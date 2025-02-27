# 부하 테스트 시나리오 작성
- 크게 두 가지 상황으로 나누어서 각각의 상황에서 테스트할 대상 및 목적, 시나리오를 작성했습니다.
- Scenario 1. 프로젝트에서 시스템 오픈 전 성능 테스트
    - 테스트 대상 : 상품 조회 및 주문/결제 시나리오
    - 목적 : 시스템 오픈 전 운영 서버 스펙을 결정하고 싶습니다.
    - 테스트 방안 : TPS 1000 을 버틸 수 있는지 검증
        - VU (가상 유저) 1000명이 1) 상품 목록 조회 후 2) 상품을 주문
- Scenario 2. 운영 중인 시스템에 배포 전 성능 테스트
    - 테스트 대상 : 선착순 쿠폰 발급 시나리오
    - 목적 : 이벤트 게시 시점에 사용자가 몰렸을 때 시스템이 버틸 수 있는지 검증하고 싶습니다.
    - 테스트 방안 : 피크 테스트를 수행해서, Redis 로 쿠폰 발급 요청에 문제가 없는지 확인합니다.
        - VU (가상 유저) 2000명이 1) 쿠폰 발급 요청

# 부하 테스트 도구 분석 및 선정
- k6 ([홈페이지](https://k6.io/) / [공식문서](https://grafana.com/docs/k6/latest/) / [참고블로그](https://kingofbackend.tistory.com/289#google_vignette))
    - Grafana k6, Grafana 사에서 제공하는 퍼포먼스(=성능) 테스트 도구
    - JavaScript 기반으로 테스트 스크립트를 작성할 수 있음
    - 코드 샘플
    ```javascript
        // k6 new 로 템플릿 사용
        import http from 'k6/http';
        import { sleep, check } from 'k6';
        
        export const options = {
        vus: 10,
        duration: '30s',
        };
        
        export default function() {
        let res = http.get('http://localhost:8080/products');
        check(res, { "status is 200": (res) => res.status === 200 });
        sleep(1);
        }
    ```
- JMeter ([홈페이지](https://jmeter.apache.org/) / [가이드문서](https://jmeter.apache.org/usermanual/get-started.html#running))
    - 100% 자바 어플리케이션
    - 브라우저가 아님
        - HTML 페이지에서 확인되는 JavaScript 를 실행하지 않음
        - HTML 페이지를 렌더링하지 않음
    - JMeter 의 주요 요소
        - Thread Group : 쓰레드 관리. 가상의 사용자 수에 해당하는 쓰레드 수를 설정, 관리
        - Samplers : 요청을 추상화한 것 (FTP, HTTP, SMTP 등)
        - Logic Controller : 요청들을 묶어서 시나리오화한 것. 실제 유저의 행동과 유사하게 처리
        - Listeners : 테스트 결과를 추상화한 것
- nGrinder ([홈페이지](https://naver.github.io/ngrinder/) / [GitHub](https://github.com/naver/ngrinder) / [참고블로그](https://leezzangmin.tistory.com/42))
    - JVM 및 java 기반의 부하 테스트 도구 
- Artillery ([홈페이지](https://www.artillery.io/) / [가이드문서](https://www.artillery.io/docs/get-started/get-artillery) / [참고블로그](https://techblog.tabling.co.kr/artillery%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%B6%80%ED%95%98-%ED%85%8C%EC%8A%A4%ED%8A%B8-9d1f6bb2c2f5))
    - npm 으로 설치
    - YAML 기반으로 테스트 스크립트 작성, JavaScript 로도 작성할 수 있음
- **k6로 성능 테스트를 진행하기로 결정했습니다.**