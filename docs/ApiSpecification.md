# API 명세
- [포인트 충전](#------)
    * [Request Body](#request-body)
    * [Response Body](#response-body)
- [잔액 조회](#-----)
    * [Response Body](#response-body-1)
- [상품 조회](#-----)
    * [Request Parameter](#request-parameter)
    * [Response Body](#response-body-2)
        + [Product](#product)
- [인기 판매 상품 조회](#-----------)
    * [Response Body](#response-body-3)
        + [Product](#product-1)
- [상품 주문 및 결제](#----------)
    * [Request Body](#request-body-1)
        + [Product](#product-2)
    * [Response Body](#response-body-4)
        + [Product](#product-3)
- [선착순 쿠폰 발급](#---------)
    * [Request Body](#request-body-2)
    * [Response Body](#response-body-5)
        + [Coupon](#coupon)
- [보유 쿠폰 목록 조회](#-----------)
    * [Request Parameter](#request-parameter-1)
    * [Response Body](#response-body-6)
        + [Coupon](#coupon-1)
# 포인트 충전

## Request Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| amount | 충전 금액 | Integer | 필수 | 100 |

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| balance | 잔액 | Integer | 필수 | 100 |
| updatedAt | 수정일시 | String | 필수 | 2025-01-01 09:00:00 |

```markdown
POST /users/{userId}/points/charge
Request
{
  "amount": 100
}

Response
200
{
  "balance": 100,
  "updatedAt": "2025-01-01 09:00:00"
}

400
{
  "code": "1001",
  "message": "충전 금액이 유효하지 않습니다. (1 이상 백만 이하)"
}

400
{
  "code": "1002",
  "message": "포인트 잔액은 백만을 초과할 수 없습니다."
}
```

# 잔액 조회

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| balance | 잔액 | Integer | 필수 | 100 |
| updatedAt | 수정일시 | String | 필수 | 2025-01-01 09:00:00 |

```markdown
GET /users/{userId}/points

Response
200
{
  "balance": 100,
  "updatedAt": "2025-01-01 09:00:00"
}
```

# 상품 조회

## Request Parameter
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| size | 페이지 크기 | Integer | 선택 | 10 |
| page | 페이지 번호 | Integer | 선택 | 0 |
| status | 상품상태 | String | 선택 | AVAILABLE or UNAVAILABLE |

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| products | 상품 목록 | List | 필수 | - |

### Product
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 상품ID | Integer | 필수 | 1 |
| name | 상품명 | String | 필수 | 상품01 |
| unitPrice | 단위 가격 | Integer | 필수 | 500 |
| remainingQuantity | 잔여 수량 | Integer | 필수 | 20 |
| status | 판매 상태 | String | 필수 | AVAILABLE or UNAVAILABLE |

```markdown
GET /products?size=10&page=0&status=

Response
200
{
  "products": [
    {
      "id": 1,
      "name": "상품01",
      "unitPrice": 500,
      "remainingQuantity": 20,
      "status": "AVAILABLE"
    },
    ...
  ]
}
```

# 인기 판매 상품 조회
## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| products | 상품 목록 | List | 필수 | - |

### Product
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 상품ID | Integer | 필수 | 1 |
| name | 상품명 | String | 필수 | 상품01 |
| unitPrice | 단위 가격 | Integer | 필수 | 500 |
| remainingQuantity | 잔여 수량 | Integer | 필수 | 20 |
| status | 판매 상태 | String | 필수 | AVAILABLE or UNAVAILABLE |
| cumulativeSalesQuantity | 누적 판매량 | Integer | 필수 | 100 |

```markdown
GET /products/popular

Response
200
{
  "products": [
    {
      "id": 1,
      "name": "상품1",
      "unit_price": 500,
      "remainingQuantity": 20,
      "status": "AVAILABLE",
      "cumulativeSalesQuantity": 100
    },
    ...
  ]
}
```

# 상품 주문 및 결제

## Request Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| products | 상품 목록 | List | 필수 | - |
| couponId | 발급 쿠폰ID | Integer | 선택 | 1 |

### Product
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 상품ID | Integer | 필수 | 1 |
| quantity | 주문 수량 | Integer | 필수 | 5 |

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| orderId | 주문ID | Integer | 필수 | 1 |
| products | 주문 상품 목록 | List | 필수 | - |
| totalPrice | 총 주문 금액 | Integer | 필수 | 3400 |
| paymentPrice | 결제 금액 | Integer | 필수 | 3060 |
| orderedAt | 주문일시 | Integer | 필수 | 2025-01-01 15:00:00 |

### Product
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 상품ID | Integer | 필수 | 1 |
| name | 상품명 | String | 필수 | 상품01 |
| unitPrice | 단위 가격 | Integer | 필수 | 500 |
| quantity | 주문 수량 | Integer | 필수 | 5 |

```markdown
POST /user/{userId}/order

Request
{
  "products": [
    {
      "id": 1,
      "quantity": 5
    },
    {
      "id": 2,
      "quantity": 3
    }
  ],
  "couponId": 1
}

Response
{
  "orderId": 1,
  "products": [
    {
      "id": 1,
      "name": "상품01",
      "unitPrice": 500,
      "quantity": 5
    },
    {
      "id": 2,
      "name": "상품02",
      "unitPrice": 300,
      "quantity": 3
    }
  ],
  "totalPrice": 3400,
  "paymentPrice": 3060,
  "orderedAt": "2025-01-01 15:00:00"
}

400
{
  "code": "2001",
  "message": "주문 수량이 유효하지 않습니다."
}

400
{
  "code": "2002",
  "message": "주문 상품이 존재하지 않습니다."
}

400
{
  "code": "2003",
  "message": "주문 상품의 재고가 충분하지 않습니다."
}

400
{
  "code": "2101",
  "message": "쿠폰을 사용할 수 없습니다."
}

400
{
  "code": "2201",
  "message": "포인트 잔액이 부족합니다."
}
```

# 선착순 쿠폰 발급

## Request Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| userId | 사용자ID | Integer | 필수 | 1 |

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| coupon | 쿠폰 정보 | Object | 필수 | - |

### Coupon
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 쿠폰ID | Integer | 필수 | 1 |
| discountRate | 할인율 | Integer | 필수 | 10 |
| expiresAt | 사용기한 | String | 필수 | 2026-01-01 09:00:00 |
| createdAt | 발급일시 | String | 필수 | 2025-01-01 09:00:00 |

```markdown
POST /coupon-templates/{couponTemplateId}/issue

Request
{
  "userId": 1
}

Response
{
  "coupon": {
    "id": 1,
    "discountRate": "10",
    "expiresAt": "2026-01-01 09:00:00",
    "createdAt": "2025-01-01 09:00:00"
  }
}
```

# 보유 쿠폰 목록 조회

## Request Parameter
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| size | 페이지 크기 | Integer | 선택 | 10 |
| page | 페이지 번호 | Integer | 선택 | 0 |
| status | 상품상태 | String | 선택 | USABLE or USED or EXPIRED |

## Response Body
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| coupons | 쿠폰 목록 | List | 필수 | - |

### Coupon
| Name | Meaning | Type | 필수/선택 | 예시 |
| --- | --- | --- | --- | --- |
| id | 쿠폰ID | Integer | 필수 | 1 |
| discountRate | 할인율 | Integer | 필수 | 10 |
| status | 상품상태 | String | 필수 | USABLE or USED or EXPIRED |
| usedAt | 사용일시 | String | 선택 | null |
| expiresAt | 사용기한 | String | 필수 | 2026-01-01 09:00:00 |
| createdAt | 발급일시 | String | 필수 | 2025-01-01 09:00:00 |

```markdown
GET /users/{userId}/coupons?size=10&page=0&status=

Response
{
  "coupons": [
    {
      "id": 1,
      "discountRate": "10",
      "status": "USABLE",
      "usedAt": null,
      "expiresAt": "2026-01-01 09:00:00",
      "createdAt": "2025-01-01 09:00:00"
    },
    {
      "id": 2,
      "discountRate": "10",
      "status": "USED"
      "usedAt": "2025-01-01 12:00:00",
      "expiresAt": "2026-01-01 09:00:00",
      "createdAt": "2025-01-01 09:00:00"
    },
    {
      "id": 3,
      "discountRate": "10",
      "status": "EXPIRED",
      "usedAt": null,
      "expiresAt": "2024-12-31 23:59:59",
      "createdAt": "2023-12-31 23:59:59"
    }
  ]
}
```