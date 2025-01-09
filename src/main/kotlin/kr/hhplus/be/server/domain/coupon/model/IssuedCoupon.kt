package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime

class IssuedCoupon(
    val id: Long,
    val template: CouponTemplate,
    val ownedBy: Long,
    val expiresAt: LocalDateTime,
    var usedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun getStatus(at: LocalDateTime): CouponStatus {
        if (usedAt != null) {
            return CouponStatus.USED
        }
        if (at > expiresAt) {
            return CouponStatus.EXPIRED
        }
        return CouponStatus.USABLE
    }
    fun use(userId: Long, at: LocalDateTime) {
        require(userId == ownedBy) { throw CustomException(CustomExceptionType.INVALID_COUPON) }
        require(getStatus(at) == CouponStatus.USABLE) { throw CustomException(CustomExceptionType.INVALID_COUPON) }

        usedAt = at
    }
}