### 인덱스 개요
- 인덱스란 데이터베이스 테이블의 검색 속도를 향상시키기 위한 자료구조입니다.
- MySQL 8.x은 인덱스를 구현하기 위해서 B+ 트리를 사용합니다.
  - 트리는 계층형의 자료구조로, 다음의 제약 조건을 가집니다. <br/>
    루트 노드를 제외한 모든 노드는 단 하나의 부모 노드를 가짐 <br/>
    리프 노드를 제외한 모든 노드는 하나 이상의 부모 노드를 가짐
  - 이에 따라 순환 구조가 발생하지 않으며, 재귀적 탐색에 용이하다는 특징을 가집니다.
  - 대표적으로 이진 탐색 트리의 경우, 특정 노드의 왼쪽 자식은 작은 값을, 오른쪽 자식은 큰 값을 가지며, <br/>
    이 특징을 활용하면 탐색 시간을 𝑂(ℎ)만큼 줄일 수 있습니다.
  - 그리고 h를 줄이기 위한 방법 중 하나의 노드에 여러 건의 데이터가 배치시킨 트리를 **B 트리**라고 합니다.
  - 여기에 순차 검색이 어렵다는 부분을 개선한 게 데이터를 리프 노드에만 위치시켜 연결 리스트로 연결한 **B+ 트리** 입니다.

### 나의 시나리오에서 수행하는 쿼리들을 수집
- `spring.jpa.show-sql=true` 로 설정해 쿼리를 표준 출력으로 조회할 수 있도록 했고, <br/>
  이를 내부적으로 org.hibernate.SQL 에서 로깅하고 있으므로, logback.xml 설정을 통해 별도 파일로 저장하도록 했습니다.

```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>logs/sql.log</file>
        <encoder>
            <pattern>%msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="org.hibernate.SQL" level="DEBUG" additivity="false">
        <appender-ref ref="FILE"/>
    </logger>
</configuration>
```

### WHERE 절과 GROUP BY 절을  중심으로 인덱스를 고려할만한 쿼리 선별

- 대부분의 쿼리는 Primary Key 를 중심으로 한 조회를 사용하고 있습니다.
- MySQL은 테이블 생성 시 Clustered Index 라고 부르는 인덱스를 기본적으로 생성합니다.
- 테이블마다 1개씩 존재하는 Clustered Index 는 Primary Key 가 지정된 경우 해당 컬럼에 대해, 없는 경우 Unique + Not Null 조건을 만족하는 컬럼에 대해 자동으로 생성합니다.
- 별도의 인덱스를 생성할만한 쿼리로는 다음을 선별했습니다.
    - 상품 목록 조회 `SELECT * FROM product WHERE remaining_quantity > 0`
    - 인기 상품 목록 조회
      ```
        SELECT op.product_id, SUM(op.quantity)
        FROM order_product op
        WHERE op.created_at BETWEEN :start AND :end
        GROUP BY op.product_id
        ORDER BY SUM(op.quantity) DESC
        LIMIT 5
      ```

### 테스트 환경 설정 및 쿼리 성능의 변화 비교

- 데이터가 충분히 많이 있어야 쿼리 성능의 변화를 검증할 수 있다고 판단하여, <br/>
  MySQL에 검증을 위한 test 데이터베이스를 생성하였고, 상품(product)와 주문상품(order_product) 테이블을 생성했습니다.
- 상품 테이블에 300만 건, 주문 상품 테이블에 800만 건의 데이터를 생성했을 때, 각 쿼리의 조회 속도는 다음과 같았습니다.
    - 판매 가능한 상품 목록 조회 : 변경 없음
        ```sql
        CREATE INDEX IDX_P_RQ ON PRODUCT (remaining_quantity);
        ```

    - 인기 상품 목록 조회: <br/>
      실행 시간은 9총에서 283밀리초로 감소했으며, <br/>
      읽어야 하는 예측 행 수도 8백만 건에서 6만 건으로 감소했음

        ```sql
        CREATE INDEX IDX_OP_PI ON ORDER_PRODUCT (product_id); --동작하지 않음
        
        CREATE INDEX IDX_OP_PI ON ORDER_PRODUCT (created_at, product_id); --동작하지 않음

        CREATE INDEX IDX_OP_CA_PI_Q ON ORDER_PRODUCT (created_at, product_id, quantity); --RANGE 스캔
        ```