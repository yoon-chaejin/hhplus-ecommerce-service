package kr.hhplus.be.server.common.exception

import org.springframework.http.HttpStatus

enum class CustomExceptionType (val status: HttpStatus, val resultCode: String, val resultMessage: String) {
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "1001", "충전 금액이 유효하지 않습니다. (1 이상 백만 이하)"),
    INVALID_BALANCE(HttpStatus.BAD_REQUEST, "1002", "포인트 잔액은 백만을 초과할 수 없습니다."),

    INVALID_ORDER_PRODUCT_QUANTITY(HttpStatus.BAD_REQUEST, "2001", "주문 수량이 유효하지 않습니다."),
    ORDER_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "2002", "주문 상품이 존재하지 않습니다."),
    NOT_ENOUGH_QUANTITY(HttpStatus.BAD_REQUEST, "2003", "주문 상품의 재고가 충분하지 않습니다."),
    INVALID_COUPON(HttpStatus.BAD_REQUEST, "2101", "쿠폰을 사용할 수 없습니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "2201", "포인트 잔액이 부족합니다."),


    COUPON_TEMPLATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "3001", "쿠폰 템플릿이 존재하지 않습니다."),
    COUPON_ISSUE_FAILED(HttpStatus.BAD_REQUEST, "3002", "쿠폰 발급에 실패했습니다."),

}