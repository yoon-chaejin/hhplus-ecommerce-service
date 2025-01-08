package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime

class CouponTemplate(
    val id: Long,
    val discountRate: Int,
    var issueCount: Int,
    val maxIssueCount: Int,
    val issuableUntil: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun issueCoupon(userId: Long, at: LocalDateTime): IssuedCoupon {
        require(at <= issuableUntil) { throw CustomException(CustomExceptionType.COUPON_ISSUE_FAILED) }
        require(issueCount < maxIssueCount) { throw CustomException(CustomExceptionType.COUPON_ISSUE_FAILED) }

        issueCount += 1
        return IssuedCoupon(
            id = 0,
            templateId = id,
            ownedBy = userId,
            expiresAt = at.plusDays(365),
            usedAt = null,
            createdAt = at,
            updatedAt = at
        )
    }
}