package kr.hhplus.be.server.controller.coupon.model

import kr.hhplus.be.server.domain.coupon.model.CouponStatus
import java.time.LocalDateTime

data class CouponResponse(
    val id : Long,
    val discountRate: Int,
    val status : CouponStatus,
    val usedAt: LocalDateTime?,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime,
)