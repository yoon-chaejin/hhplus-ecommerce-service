package kr.hhplus.be.server.domain.coupon.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime

@Entity
class CouponTemplate(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val discountRate: Int,
    var issueCount: Int,
    val maxIssueCount: Int,
    val issuableUntil: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun issueCoupon(userId: Long, at: LocalDateTime): IssuedCoupon {
        require(at <= issuableUntil) { throw CustomException(CustomExceptionType.COUPON_ISSUE_FAILED) }
        require(issueCount < maxIssueCount) { throw CustomException(CustomExceptionType.COUPON_ISSUE_FAILED) }

        issueCount += 1
        return IssuedCoupon(
            id = 0,
            template = this,
            ownedBy = userId,
            expiresAt = at.plusDays(365),
            usedAt = null,
            createdAt = at,
            updatedAt = at
        )
    }
}