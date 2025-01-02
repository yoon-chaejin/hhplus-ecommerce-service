package kr.hhplus.be.server.common.exception

import org.springframework.http.HttpStatus

enum class CustomExceptionType (val status: HttpStatus, val resultCode: String, val resultMessage: String) {
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "1001", "충전 금액이 유효하지 않습니다. (1 이상 백만 이하)"),
    INVALID_BALANCE(HttpStatus.BAD_REQUEST, "1002", "포인트 잔액은 백만을 초과할 수 없습니다.")
}