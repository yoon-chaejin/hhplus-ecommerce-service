package kr.hhplus.be.server.point.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime

@Entity
class Point(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val userId: Long,
    var balance: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    ) {

    companion object {
        val MIN_BALANCE = 0
        val MAX_BALANCE = 1_000_000

        val MIN_AMOUNT = 1
        val MAX_AMOUNT = 1_000_000
    }

    init {
        require(balance >= MIN_BALANCE) { throw IllegalArgumentException("잔액은 0 이상이어야 합니다.") }
        require(balance <= MAX_BALANCE) { throw IllegalArgumentException("잔액은 백만 이하여야 합니다.") }
    }

    fun plus(amount: Int) {
        require(amount >= MIN_AMOUNT) { throw CustomException(CustomExceptionType.INVALID_CHARGE_AMOUNT)}
        require(amount <= MAX_AMOUNT) { throw CustomException(CustomExceptionType.INVALID_CHARGE_AMOUNT)}
        balance += amount

        require(balance <= MAX_BALANCE) { throw CustomException(CustomExceptionType.INVALID_BALANCE)}
    }

    fun minus(amount: Int) {
        require(amount > 0) { throw IllegalArgumentException("차감 금액은 1 이상이어야 합니다.)") }
        require(balance >= amount) { throw CustomException(CustomExceptionType.NOT_ENOUGH_POINT) }
        balance -= amount
    }
}