# 항해99 플러스 (백엔드) 이커머스 서비스

## 시나리오 분석
- 유즈케이스
    - 상품 주문에 필요한 **메뉴 정보**들을 **구성**하고 **조회**가 가능해야 합니다.
    - 사용자는 **상품을 여러개 선택**해 **주문**할 수 있고, 미리 **충전**한 **잔액**을 **이용**합니다.
    - **상품 주문 내역**을 통해 **판매량**이 가장 높은 상품을 **추천**합니다.
    - **사용자**는 선착순으로 **할인 쿠폰**을 **발급**받을 수 있습니다.
    - 주문 시에 유효한 할인 쿠폰을 함께 제출하면, **전체 주문금액**에 대해 **할인 혜택**을 부여받을 수 있습니다.
- API 목록
    - 잔액 충전 : 사용자 식별자, 충전할 금액
    - 잔액 조회 : 사용자 식별자
    - 상품 조회 : 상품 정보 (ID, 이름, 가격, 잔여수량)
    - 상품 주문&결제 : 사용자 식별자, 상품 목록 (ID, 수량), 쿠폰ID
        - 데이터 분석을 위해 결제 성공 시에 실시간으로 주문 정보를 데이터 플랫폼에 전송해야 합니다.
        - 1) 상품 수량 감소 2) 주문 정보 생성 3) 잔액 차감 4) 쿠폰 사용 처리 5) 주문 정보 전송
        - cf. 상품 재고 유무 / 잔액 유무 / 쿠폰 소유 여부
    - 인기 판매 상품 조회
        - 최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 제공하는 API
    - 선착순 쿠폰 발급 : 사용자 식별자
    - 보유 쿠폰 목록 조회 : 사용자 식별자
- 고려사항
    - 기능별 단위테스트 1개 이상 작성
    - 다수의 인스턴스 환경 고려
    - 동시성 이슈 고려
    - 재고 관리에 이슈가 없도록
    - 동시에 여러 주문이 들어올 경우, 유저의 보유 잔고에 대한 처리가 정확해야 합니다.
    - 각 상품의 재고 관리가 정상적으로 이루어져 잘못된 주문이 발생하지 않도록 해야 합니다.

## 이벤트 스토밍 기반 도메인 설계
![이벤트 스토밍  도메인 설계_v0 1 0](https://github.com/user-attachments/assets/e17bdcd5-356a-4adc-96d4-af5bc1077fac)
### 포인트
- 충전 금액은 0 이상, 백만 이하 이어야 한다
- 잔액은 백만을 넘을 수 없다.
### 쿠폰
- 발급 전 쿠폰 정보는 "쿠폰 템플릿"에서, 발급 후 쿠폰 정보는 "발급 쿠폰"에서 관리한다.
- 발급 쿠폰은 쿠폰 템플릿의 최대 발급 개수에 맞춰 선착순으로 발급된다.
- 쿠폰 상태는 사용 가능, 사용 완료, 기한 만료로 구분한다.
- 쿠폰 템플릿은 발급기한이 존재하고, 발급 쿠폰은 사용기한이 존재한다.
- 쿠폰의 사용기한은 발급일시부터 1년(365일) 후로 설정한다.
### 상품
- 상품은 최소 5개 이상 등록되어 있다고 가정한다.
- 상품 가격은 100 단위로 정해진다고 가정한다.
- 상품 상태는 판매중, 판매 중단으로 구분한다.
- 인기 상품 목록은 하루에 한 번 갱신한다.
### 주문
- 상품별 주문 수량은 0보다 커야 한다.
- 상품별 주문 수량은 상품 잔여 수량을 초과할 수 없다.
- 쿠폰을 사용하는 경우, 주문자와 쿠폰 소유자는 같아야 한다.

## 주요 프로세스에 대한 다이어그램 작성
### [Flow Chart] 포인트 충전
![Image](https://github.com/user-attachments/assets/68b82159-de4c-410b-bffd-c40acc1b253c)

### [Flow Chart] 쿠폰 발급
![Image](https://github.com/user-attachments/assets/b1d263ce-b4ab-4aea-a342-618427b513f5)

### [Flow Chart] 주문 결제
![Image](https://github.com/user-attachments/assets/f0f95402-a29a-41a4-9a6a-1982317269b1)

### [Sequence Diagram] 주문 결제
![Image](https://github.com/user-attachments/assets/ee851a28-795e-439a-8208-a940e32801b6)
