# 1. 동시성 문제
<details>  
<summary>항해 1, 2주차에 동시성 문제에 대해 찾아본 내용을 다시 정리해보면</summary>

- 동시성 문제는 **여러 Process나 Thread가 동시에 자원에 접근하거나 작업을 수행할 때 발생할 수 있는 예기치 않은 문제**입니다.
- 이 떄, 동시성 문제를 고려하는 맥락(공유 자원, 임계 구역)이 중요합니다.
---
### JVM 환경에서 동시성 문제가 발생하는 맥락을 살펴보면, 다음과 같습니다.
  - 멀티 코어 환경에서 Thread 사용 시, 코어 당 하나의 Thread 처리합니다.
    Thread는 STACK만 분리하고, HEAP, CODE, DATA 영역은 공유하며,
    따라서 HEAP, DATA에 적재된 자원에 대해서 동시성 문제가 발생하게 됩니다.
  - JVM 은 단일 프로세스로 실행되지만 기본적으로 멀티 Threading 활용할 수 있으며,
    이 때, JVM Thread와 OS Thread를 매핑하는 방식인 
    Threading 모델 *(=OS Thread와 매핑하는 방식)* 은 JVM마다 차이가 있을 수 있습니다.
---
### Spring + Web Application 에서 발생하는 동시성 문제가 발생하는 맥락을 살펴보면, 다음과 같습니다.
  - Spring 은 요청마다 Thread를 생성 또는 할당하는 방식을 사용합니다. 
    그리고 통상적으로 Web Application은 별도의 스토리지(ex. RDB)에 데이터를 적재하며,
    이 때, 특정 데이터에 대해 여러 요청이 CRUD 하는 과정에서 동시성 문제가 발생할 수 있습니다.
  - 단일 서버 환경이라면 Application 내에서 동시성 문제를 제어할 수 있겠지만,
    분산 서버 환경에서는 Database 에서 제공하는 기능들을 활용해 동시성 문제를 제어하게 됩니다.
---
### Database 에서 발생할 수 있는 동시성 문제를 살펴보면, 다음과 같습니다. (위키피디아 [참고](https://en.wikipedia.org/wiki/Concurrency_control))
  - lost update problem (=overwriting uncommited data)
    - 1번, 2번 트랜잭션이 각각 A 데이터에 쓰기 작업을 하고, B 데이터에 쓰기 작업을 하는 경우, 
      (A1, B1) (A1, B2) (A2, B1) (A2, B2) 의 경우로 끝나는 상황이 발생할 수 있음
    ```
    Tx1 : T1.start() → Update(A v1) → Update(B v1) → T1.commit()
    Tx2 : T2.start() → Update(B v2) → Update(B v2) → T2.commit()
    - expected : A v1 & B v1 또는 A v2 & B v2
    - failed : A v2 & B v1 또는 A v1 & B v2
    ```
  - dirty read problem (reading uncommited data)
    - 1번, 2번 트랜잭션이 각각 A 데이터를 조회해서 A 데이터를 변경하는 경우,
      (T1만 적용됨) (T1 T2가 적용됨) (T2가 적용됨) 의 경우로 끝나는 상황이 발생할 수 있음
    ```
    Tx1 : T1.start() → Read(A) → Update(A v1) → Read(B) → Update(B v1) → T1.commit()
    Tx2 : T2.start() → Read(A) → Update(A v2) → Read(B) → Update(B v2) → T2.commit()
    - expected : Read(A) → A v0 / Read(B) → B v0
    - failed : Read(A) → A v1 또는 A v2 / Read(B) → B v1 또는 B v2
    ```
  - incorrect summary problem (= inconsistency analysis)
    - summary 하는 트랜잭션이 진행 중인데 update 가 일어나 summary 값이 일관되지 않는 문제
  - Uncommitted Dependency : 
    - Rollback 된 데이터를 Rollback 전에 읽어 생기는 문제
</details>

## 1-1. Database 동시성 문제 톺아보기
- MySQL READ COMMITED 로 변경했을 때 트랜잭션 내에서 동일 쿼리의 결과가 달라지는 것을 확인할 수 있습니다.
<details>
<summary>SQL 문 확인하기</summary>

```sql
# Phantom Read @ MySQL 8.0.40
# 격리수준을 READ COMMITTED 로 변경하고 INSERT 한 경우

#given
Tx1 : SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
Tx1 : START TRANSACTION ;

Tx2 : SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
Tx2 : START TRANSACTION ;

#when
Tx1 : SELECT * FROM data ; #then : data 1건 조회
Tx2 : INSERT INTO DATA (~) VALUES (~) ;
Tx2 : COMMIT ;

Tx1 : SELECT * FROM data ; #then : data 2건 조회
Tx1 : COMMIT ;
```

```sql
#Phantom Read @ MySQL 8.0.40
#격리수준을 READ COMMITTED 로 변경하고 DELETE 한 경우

#given
Tx1 : SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
Tx1 : START TRANSACTION ;

Tx2 : SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
Tx2 : START TRANSACTION ;

#when
Tx1 : SELECT * FROM data ; #then : data 1건 조회
Tx2 : DELETE FROM data ;
Tx2 : COMMIT ;

Tx1 : SELECT * FROM data ; #then : data 2건 조회
Tx1 : COMMIT ;
```
</details>

- 추가로, MySQL 에서도 특수한 경우에 Phantom Read가 발생하는 것을 확인할 수 있습니다.

<details>
<summary>SQL 문 확인하기</summary>

```sql
# Phantom Read @ MySQL 8.0.40
# REPEATABLE READ 에서도 Phantom Read 가 발생할 수 있는 경우
# [참고] https://velog.io/@glencode/트랜잭션과-ACID-그리고-MySQL-InnoDB에서-Phantom-Read

# SELECT @@GLOBAL.transaction_isolation; #REPEATABLE_READ
# SELECT @@SESSION.transaction_isolation; #REPEATABLE_READ

Tx1 : START TRANSACTION ;
Tx2 : START TRANSACTION ;

Tx2 : SELECT * FROM data ;

Tx1 : INSERT INTO data VALUES (1, '종협 코치님', 31) ;
Tx1 : COMMIT ;

Tx2 : SELECT * FROM data ; #안 보임 : MVCC 로 Phantom Read 가 발생하지 않음

# 아래 구문 모두 Tx1의 영향을 받음
# Case 1 : 조회는 여전히 불가
Tx2 : INSERT INTO data VALUES (1, '허재 코치님', 30); # 대기하다가 commit 되면 Duplicate Key
Tx2 : DELETE FROM data WHERE id = 1; # 1 rows affected. 조회 불가

Tx2 : SELECT * FROM data FOR SHARE; # Phantom Read 발생
Tx2 : SELECT * FROM data FOR UPDATE; # Phantom Read 발생 

# Case 2 : 조회 결과 변경됨
Tx2 : UPDATE data SET age = '32' where name = '종협 코치님'; #then 1 update : Phantom!?!
Tx2 : SELECT * FROM data ; #update 후 보임 : Phantom Read 발생

Tx2 : COMMIT ;
```
</details>

# 2. 동시성 제어

- 동시성 제어를 위해 락을 사용하는 방법에는 낙관락, 비관락, 분산락 등이 있습니다. ([참고](https://velog.io/@dangddoong/learning-solutions-concurrency-issues-e-commerce-inventory-management-logic))
- 비관적 락
  - Database 차원에서 제공하며, 실패 시 DB단 에러 확인 가능합니다.
  - 공유락 (=S-Lock, Shared Lock) 과 배타락 (=X-Lock, Exclusive Lock)이 있으며,
    공유락은 `SELECT ~ FOR SHARE` 구문을, 배타락은 `SELECT ~ FOR UPDATE` 구문으로 획득할 수 있습니다.
  - 공유락이 걸린 데이터는 해당 데이터를 결과에 포함하는 `SELECT` 와 `SELECT ~ FOR SHARE`은 허용하지만 `SELECT ~ FOR UPDATE`은 대기하게 합니다.
  - 배타락이 걸린 데이터는 해당 데이터를 결과에 포함하는 `SELECT`는 허용하지만 `SELECT ~ FOR SHARE` 와 `SELECT ~ FOR UPDATE`는 대기하게 합니다.
  - 추가로 테이블에 락을 거는 경우 `LOCK TABLE ~ READ` 구문과 `LOCK TABLE ~ WRITE` 구문을 사용할 수 있습니다.
- 낙관적 락 : Version 용 컬럼을 통해 동시성을 제어하는 방안 → 실패 시 DB에서는 0 rows affected
    - 테이블 내부에 버전관리 만을 위한 필드가 추가됨
    ```sql
    # 2번 낙관적 락
    T1 -> SELECT * FROM users WHERE userId = 1;
    T2 -> SELECT * FROM users WHERE userId = 1;
    T1 -> UPDATE users SET point = 700, version = 1 WHERE userId = 1 AND version = 0;
    T1 COMMIT;
    # T1 은 성공!

    T2 -> UPDATE users SET point = 1000, version = 1 WHERE userId = 1 AND version = 0;
    # DB에서 return 0

    # Spring Data 단에서 Update 0 이라 ObjectOptimisticLockingFailureException 발생
    # 재시도 처리하거나 오류나서 Rollback 되거나 하도록 Application 단에서 제어
    ```

- 별개로, Named Query와 `@Modifying`을 활용해 DB단 원자적 연산을 보장하는 방법이 있으나, 도메인의 정책과 기능이 DB에 의존적이게 된다는 단점이 있습니다.
    - [참고1](https://velog.io/@dangddoong/%EC%87%BC%ED%95%91%EB%AA%B0-%EC%9E%AC%EA%B3%A0%EA%B4%80%EB%A6%AC-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-Query%EB%A1%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-Lock-%EA%B5%AC%EA%B0%84%EC%9D%84-%EC%B5%9C%EC%86%8C%ED%99%94%ED%95%98%EA%B8%B0)
    - [참고2](https://frogand.tistory.com/174)

# 3.시나리오 분석
- 동시성 문제가 발생 가능한 시나리오
1. 쿠폰 도메인
    1. 쿠폰 발급 : 여러 사람이 하나의 자원(CouponTemplate)에 접근 가능. **배타적 락 사용**
        - 선착순 쿠폰이라는 점에 있어서 요청이 몰릴 가능성이 높음. 대기열 or 분산 락을 고려해볼만 함.
        - 예상
            1. CouponTemplate 조회
            2. CouponTemplate 변경
            3. IssuedCoupon 생성
        - 실제
            1. CouponTemplate 조회
            2. IssuedCoupon 생성
            3. CouponTemplate 변경 (⇒ Dirty Checking)
            <details>
            <summary>테스트 코드 실행 결과</summary>

                ```sql
                # 쿠폰 최대 발급 개수가 10개이고, 쿠폰 발급 요청이 20건 들어왔을 때, 10건만 성공한다
                # 실행되는 결과를 보니 Pool Size 가 3개인가? 했음... -> 맞음...
                (S1) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S2) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S3) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (I1) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (U1) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I2) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S4) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U2) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I3) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S5) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U3) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I4) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S6) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U4) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I5) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S7) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U5) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I6) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S8) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U6) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I7) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S9) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U7) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I8) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S10) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U8) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I9) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S11) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U9) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I10) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S12) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U10) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (S13) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S14) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S15) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S16) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S17) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S18) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S19) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S20) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                
                ```

                ```sql
                # 쿠폰 최대 발급 개수가 10개이고, 쿠폰 발급 요청이 20건 들어왔을 때, 10건만 성공한다
                # Maximum Pool Size 10개이면...
                
                (S01) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S02) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S03) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S04) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S05) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S06) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S07) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S08) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S09) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (S10) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (I01) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (U01) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I02) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (U02) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (S11) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (I03) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S12) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U03) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I04) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S13) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U04) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I05) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S14) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U05) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I06) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S15) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U06) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I07) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S16) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U07) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I08) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S17) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U08) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I09) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S18) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U09) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (I10) Hibernate: insert into issued_coupon (created_at,expires_at,owned_by,template_id,updated_at,used_at) values (?,?,?,?,?,?)
                (S19) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                (U10) Hibernate: update coupon_template set created_at=?,discount_rate=?,issuable_until=?,issue_count=?,max_issue_count=?,updated_at=? where id=?
                (S20) Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=? for update
                
                ```
            </details>
    2. 쿠폰 사용 : 한 사람이 하나의 자원(IssuedCoupon)에 접근. **낙관적 락 사용**
        - 예상
            1. IssuedCoupon 조회
            2. IssuedCoupon 변경
        - 실제
            1. IssuedCoupon 조회
            2. CouponTemplate 조회
            3. IssuedCoupon 변경
          <details>
            <summary>테스트 코드 실행 결과</summary>
          
                ```sql
                # 쿠폰 사용 요청이 2건 이상 들어왔을 때, 1건만 성공한다
                # FetchType.LAZY 이고 Connection Pool 1개 이면
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                Hibernate: update issued_coupon set created_at=?,expires_at=?,owned_by=?,template_id=?,updated_at=?,used_at=? where id=?
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                ```

                ```sql
                # FetchType.EAGER 이고 Connection Pool 1개 이면
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                Hibernate: update issued_coupon set created_at=?,expires_at=?,owned_by=?,template_id=?,updated_at=?,used_at=? where id=?
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                ```

                ```sql
                # 왜 CouponTemplate을 계속 가져오지? Proxy 객체가 아니네?
                # Transactional 때문인가? -> select 중에 오류 발생
                # Lock 때문인가? -> 
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                2025-01-23T05:41:27.334Z  INFO 29828 --- [hhplus] [pool-2-thread-1] k.h.b.s.domain.coupon.CouponService      : class kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
                2025-01-23T05:41:27.334Z  INFO 29828 --- [hhplus] [pool-2-thread-1] k.h.b.s.domain.coupon.CouponService      : class kr.hhplus.be.server.domain.coupon.model.CouponTemplate
                Hibernate: update issued_coupon set created_at=?,expires_at=?,owned_by=?,template_id=?,updated_at=?,used_at=? where id=?
                Hibernate: select ic1_0.id,ic1_0.created_at,ic1_0.expires_at,ic1_0.owned_by,ic1_0.template_id,ic1_0.updated_at,ic1_0.used_at from issued_coupon ic1_0 where ic1_0.id=? for update
                Hibernate: select ct1_0.id,ct1_0.created_at,ct1_0.discount_rate,ct1_0.issuable_until,ct1_0.issue_count,ct1_0.max_issue_count,ct1_0.updated_at from coupon_template ct1_0 where ct1_0.id=?
                2025-01-23T05:41:27.427Z  INFO 29828 --- [hhplus] [pool-2-thread-2] k.h.b.s.domain.coupon.CouponService      : class kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
                2025-01-23T05:41:27.428Z  INFO 29828 --- [hhplus] [pool-2-thread-2] k.h.b.s.domain.coupon.CouponService      : class kr.hhplus.be.server.domain.coupon.model.CouponTemplate
                
                ```
            </details>
2. 상품
    1. 재고 차감 : 여러 사람이 하나의 자원(Product)에 접근 가능. **배타적 락 사용**
        - 예상
            1. Product 조회
            2. Product 변경
        - 실제
            1. Product 조회
            2. Product 변경
            <details>
            <summary>테스트 코드 실행 결과</summary>

                ```sql
                # 상품 재고 요청이 5건 들어왔을 때, 요청 내용만큼 재고가 차감된다
                Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 limit ?,?
                
                # 테스트 범위
                (S01) Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 where p1_0.id=? for update
                (S02) Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 where p1_0.id=? for update
                (U01) Hibernate: update product set created_at=?,name=?,remaining_quantity=?,unit_price=?,updated_at=? where id=?
                (U02) Hibernate: update product set created_at=?,name=?,remaining_quantity=?,unit_price=?,updated_at=? where id=?
                (S03) Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 where p1_0.id=? for update
                (U03) Hibernate: update product set created_at=?,name=?,remaining_quantity=?,unit_price=?,updated_at=? where id=?
                (S04) Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 where p1_0.id=? for update
                (U04) Hibernate: update product set created_at=?,name=?,remaining_quantity=?,unit_price=?,updated_at=? where id=?
                (S05) Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 where p1_0.id=? for update
                (U05) Hibernate: update product set created_at=?,name=?,remaining_quantity=?,unit_price=?,updated_at=? where id=?
                
                Hibernate: select p1_0.id,p1_0.created_at,p1_0.name,p1_0.remaining_quantity,p1_0.unit_price,p1_0.updated_at from product p1_0 limit ?,?
                
                ```
            </details>

3. 포인트 : 한 사람이 하나의 자원(Point)에 접근. **낙관적 락 사용**
    1. 포인트 충전
        - 포인트 조회
        - 포인트 변경
    2. 포인트 차감
        - 포인트 조회
        - 포인트 변경
    3. 포인트 충전 & 차감
        - 포인트 조회
        - 포인트 변경

        <details>
        <summary>테스트 코드 실행 결과</summary>
   
        ```sql
        (S01) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (S02) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (S03) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U01) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (U02) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S04) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U03) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S05) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U04) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S06) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U05) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S07) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U06) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S08) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U07) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S09) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U08) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (S10) Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        (U09) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        (U10) Hibernate: update point set balance=?,created_at=?,updated_at=?,user_id=? where id=?
        
        Hibernate: select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=? for update
        ```
        </details>

## 3-1. 시나리오별 동시성 제어 방안
- 1명의 사용자(=사람)는 악의를 가진 게 아닌 이상 동시에 작업을 요청할 가능성이 낮다고 가정했습니다.
- 따라서 쿠폰 사용 및 포인트 충전/차감은 낙관적 락으로, 쿠폰 발급 및 재고 차감은 비관적(배타적) 락으로 대응할 예정입니다.
