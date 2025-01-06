package kr.hhplus.be.server.point.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime

class Point(
    val id: Long = 0L,
    val userId: Long,
    var balance: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    ) {

    init {
        require(balance >= 0) { throw IllegalArgumentException("잔액은 0 이상이어야 합니다.") }
        require(balance <= 1_000_000) { throw IllegalArgumentException("잔액은 백만 이하여야 합니다.") }
    }

    fun plus(amount: Int) {
        require(amount > 0) { throw IllegalArgumentException("충전 금액은 0 이상이어야 합니다.")}
        require(amount <= 1_000_000) { throw IllegalArgumentException("충전 금액은 백만 이하여야 합니다.")}
        balance += amount

        require(balance <= 1_000_000) { throw CustomException(CustomExceptionType.INVALID_BALANCE)}
    }
}