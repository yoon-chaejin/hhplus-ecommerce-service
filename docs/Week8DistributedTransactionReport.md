# 문제 상황
- 서비스의 규모가 확장되어 MSA(Microservice Architecture)의 형태로 각 도메인별로 배포단위를 분리하는 상황
- MSA란 기존의 Monolithic(모놀리식) 아키텍처와 SOA(Service-Oriented Architecture)에 대비하여 <br/>
  서비스별 독립성과 확장성을 강조하는 설계 방식입니다.
- MSA는 기존 설계 방식에 대비해 다음의 장점을 갖습니다. 
  1. 서비스 단위 확장성(Scalability)이 증가함
  2. 독립적인 배포가 가능하며 개발 효율성을 높일 수 있음
  3. 기술 스택을 유연하게 선택할 수 있음
- 반면 다음의 문제들을 고려해야 합니다.
  1. 분산 시스템의 복잡성이 증가함 (특히, 트랜잭션 관리 및 데이터 정합성 유지가 어려움)
  2. 네트워크 오버헤드가 증가함
  3. 운영 및 모니터링이 어려움
  4. 초기 개발 및 구축 비용이 증함
- 그 중에서도 트랜잭션 처리 및 데이터 정합성 유지에 대한 해결책을 고려해보려고 합니다.

# 트랜잭션 처리의 한계점
- 도메인별로 배포단위를 분리하게 되면 기존의 트랜잭션 처리 방식을 유지하기 어려워집니다.
  단일 저장소에서는 하나의 데이터베이스 트랜잭션(ACID)을 보장할 수 있었지만,<br/>
  각 도메인별로 데이터가 분리되면 하나의 비즈니스 요청이 여러 도메인에 걸쳐 처리될 수 있습니다.
  여러 개의 독립적인 DB 트랜잭션을 하나의 논리적 트랜잭션처럼 관리하려면 분산 트랜잭션 관리 기능이 필요합니다.
```kotlin
/* 기존 로직 */
class OrderApplication {
    @Transactional
    fun order() {
        상품_재고_차감()
        주문_상품_생성()
        쿠폰_사용()
        포인트_차감()
        주문_생성()
    }
}

```

# 기존 트랜잭션 처리에 대한 대안
1. 2PC (Two Phase Commit)
   - 코디네이터와 여러 노드(서비스, DB) 간의 합의를 통해 트랜잭션을 처리하는 방식
   - 1단계 (Prepare) 단계로 사전에 "트랜잭션 수행할 준비가 되었는지" 확인하고<br/>
     2단계 (Commit) 단계에서 "실제로 Commit 명령을 수행함"
   - 1단계에서 하나의 노드에서라도 트랜잭션 수행이 불가능하다고 응답하거나,<br/>
     1, 2단계에서 작업 중 오류가 발생하는 경우 전체 트랜잭션을 Rollback 함 
2. SAGA 패턴
   - 각 서비스가 개별적으로 트랜잭션을 수행하며, 실패 시 보상 트랜잭션을 수행하는 방식
   - 장기 실행 트랜잭션을 다루기 위한 패턴으로, 최종적 일관성(Eventual Consistency)를 보장함
   - SAGA 패턴에는 Choreography 방식과 Orchestration 방식이 있음

# 각 대안에 대한 설계
1. 2PC
```mermaidjs
sequenceDiagram
  actor U as User
  participant TC as TransactionCoordinator
  participant PrS as ProductService
  participant CS as CouponService
  participant PoS as PointService
  participant OS as OrderService

  U ->> +TC : 상품 주문 API 호출

  %% 1단계: Prepare (준비 단계)
  TC ->> +PrS : 상품 목록 검증 후 재고 변경 요청 (Prepare)
  PrS -->> -TC : OK (준비 완료)

  opt 쿠폰을 사용하는 경우
    TC ->> +CS : 쿠폰 검증 후 사용 요청 (Prepare)
    CS -->> -TC : OK (준비 완료)
  end

  TC ->> +PoS : 결제 가능 여부 확인 후 포인트 차감 요청 (Prepare)
  PoS -->> -TC : OK (준비 완료)

  TC ->> +OS : 주문 정보 생성 요청 (Prepare)
  OS -->> -TC : OK (준비 완료)

  %% 2단계: Commit (확정 단계)
  TC ->> +PrS : Commit 실행
  PrS -->> -TC : Commit 완료

  opt 쿠폰을 사용하는 경우
    TC ->> +CS : Commit 실행
    CS -->> -TC : Commit 완료
  end

  TC ->> +PoS : Commit 실행
  PoS -->> -TC : Commit 완료

  TC ->> +OS : Commit 실행
  OS -->> -TC : Commit 완료

  TC -->> -U : 주문 결과 반환
```
2. SAGA 패턴
   - SAGA 패턴으로 진행할 경우, 기존에 하나로 되어 있는 주문/결제 프로세스를 분리
   1. Choreography 방식
      - 각 로컬 트랜잭션이 다른 서비스의 로컬 트랜잭션을 이벤트 트리거하는 방식
      ```
      1. 주문 요청하면 Order 서비스는 주문 정보를 검증하고 주문 데이터를 생성해서 결과를 Product 서비스에게 이벤트로 전달한다.
      2. Product 서비스의 이벤트 핸들러가 발생한 주문 요청에 대해 재고 차감 시도한다.
      3. 재고 차감 시도한 후 결과에 대해 이벤트를 발생시킨다.
      4. Order 서비스는 결과에 따라 실패한 경우에는 보상 트랜잭션, 문제 없는 경우에는 주문 상품 정보를 생성한다.
      ```
   2. Orchestration 방식
      - 분산 트랜잭션을 책임지는 별도의 중계자가 각 서비스에 실행할 트랜잭션을 알려주는 방식
      ```
      1. 주문 요청하면 중계자가 재고 차감 명령 이벤트를 발생시킨다.
      2. Product 서비스가 재고 차감을 시도한다.
      3. 재고 차감을 시도한 결과를 이벤트로 응답한다.
      4. 재고 차감 이벤트 결과에 따라 Order 서비스에 주문 정보 생성 이벤트를 발생시킨다.
      5. 트랜잭션이 끝나면 중계자를 종료하여 전체 트랜잭션 처리를 종료한다.
      ```