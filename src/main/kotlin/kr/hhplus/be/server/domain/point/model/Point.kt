package kr.hhplus.be.server.point.model

import jakarta.persistence.*
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import org.hibernate.annotations.ColumnDefault
import kr.hhplus.be.server.common.model.BaseEntity

@Entity
class Point(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val userId: Long,
    var balance: Int = 0,
    @Version
    @ColumnDefault("0")
    val version: Long = 0,
) : BaseEntity() {

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