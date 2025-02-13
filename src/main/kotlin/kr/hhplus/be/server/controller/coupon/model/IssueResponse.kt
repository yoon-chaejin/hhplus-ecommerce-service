package kr.hhplus.be.server.controller.coupon.model

import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import java.time.LocalDateTime

data class IssueResponse(
    val id: Long,
    val discountRate: Int,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime,
)

fun IssuedCoupon.toIssueResponse(): IssueResponse {
    return IssueResponse(
        id = this.id,
        discountRate = this.discountRate,
        expiresAt = this.expiresAt,
        createdAt = this.createdAt,
    )
}