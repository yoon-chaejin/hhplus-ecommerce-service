import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '10s', target: '10'},
        { duration: '10s', target: '50'},
        { duration: '40s', target: '100'},
    ]
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    // 랜덤 사용자 ID 생성 (1~100 사이의 숫자)
    const USER_ID = Math.floor(Math.random() * 100) + 1;

    // 1. 사용자 포인트 충전
    let chargePayload = JSON.stringify({ amount: 50000 }); // 10,000 포인트 충전
    let chargeRes = http.post(`${BASE_URL}/users/${USER_ID}/points/charge`, chargePayload, {
        headers: { 'Content-Type': 'application/json' },
    });

    // 2. 상품 목록 조회
    let productsRes = http.get(`${BASE_URL}/products?status=AVAILABLE`);
    check(productsRes, {
        '상품 목록 조회 성공': (res) => res.status === 200,
    });

    let products = JSON.parse(productsRes.body).products;
    if (!Array.isArray(products) || products.length === 0) {
        console.log('상품 목록이 비어 있습니다.');
        return;
    }

    // 3. 랜덤 상품 선택
    let selectedProducts = [];
    let numOfProducts = Math.floor(Math.random() * products.length) + 1;
    for (let i = 0; i < numOfProducts; i++) {
        let randomIndex = Math.floor(Math.random() * products.length);
        selectedProducts.push({
            id: products[randomIndex].id,
            quantity: Math.floor(Math.random() * 5) + 1, // 1~5개 주문
        });
    }

    // 4. 주문 요청
    let orderPayload = JSON.stringify({ products: selectedProducts, couponId: null });
    let orderRes = http.post(`${BASE_URL}/users/${USER_ID}/orders`, orderPayload, {
        headers: { 'Content-Type': 'application/json' },
    });
    check(orderRes, {
        '주문 생성 성공': (res) => res.status === 200,
        '재고 부족': (res) => JSON.parse(res.body).error === "2003",
        '포인트 부족': (res) => JSON.parse(res.body).error === "2201",
    });

    sleep(1); // 부하 테스트를 위해 1초 대기
}
