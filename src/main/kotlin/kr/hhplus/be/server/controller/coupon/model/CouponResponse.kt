package kr.hhplus.be.server.controller.coupon.model

import kr.hhplus.be.server.domain.coupon.model.CouponStatus
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import java.time.LocalDateTime

data class CouponResponse(
    val id : Long,
    val discountRate: Int,
    val status : CouponStatus,
    val usedAt: LocalDateTime?,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime,
)

fun IssuedCoupon.toCouponResponse(): CouponResponse {
    return CouponResponse(
        id = this.id,
        discountRate = this.template.discountRate,
        status = this.getStatus(),
        usedAt = this.usedAt,
        expiresAt = this.expiresAt,
        createdAt = this.createdAt,
    )
}