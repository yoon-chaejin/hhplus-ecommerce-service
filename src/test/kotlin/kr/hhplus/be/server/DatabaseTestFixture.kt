package kr.hhplus.be.server

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import kr.hhplus.be.server.infrastructure.coupon.CouponTemplateJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.IssuedCouponJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DatabaseTestFixture (
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) {
    fun createCouponTemplate(
        id: Long = 0L,
        discountRate: Int = 10,
        issueCount: Int = 0,
        maxIssueCount: Int = 10,
        issuableUntil: LocalDateTime = LocalDateTime.now().plusDays(1),
    ): CouponTemplate {
        val template = CouponTemplate(
            id = id,
            discountRate = discountRate,
            issueCount = issueCount,
            maxIssueCount = maxIssueCount,
            issuableUntil = issuableUntil
        )
        return couponTemplateJpaRepository.saveAndFlush(template)
    }

    fun createIssuedCoupon(
        id: Long = 0L,
        template: CouponTemplate,
        usedAt: LocalDateTime? = null,
        ownedBy: Long = 1L,
        expiresAt: LocalDateTime = LocalDateTime.now().plusDays(1),
    ) : IssuedCoupon {
        val coupon = IssuedCoupon(
            id = id,
            template = template,
            usedAt = usedAt,
            ownedBy = ownedBy,
            expiresAt = expiresAt,
        )
        return issuedCouponJpaRepository.saveAndFlush(coupon)
    }
}