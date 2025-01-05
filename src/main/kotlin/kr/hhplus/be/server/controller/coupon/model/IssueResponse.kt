package kr.hhplus.be.server.controller.coupon.model

import java.time.LocalDateTime

data class IssueResponse(
    val id: Long,
    val discountRate: Int,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime,
)
