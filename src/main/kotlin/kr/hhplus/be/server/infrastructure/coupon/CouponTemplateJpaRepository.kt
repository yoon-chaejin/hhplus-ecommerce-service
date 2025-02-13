package kr.hhplus.be.server.infrastructure.coupon

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponTemplateJpaRepository: JpaRepository<CouponTemplate, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): CouponTemplate?

    @Query("select cp from CouponTemplate cp where cp.issueCount < cp.maxIssueCount")
    fun findCouponTemplatesIssuable(): List<CouponTemplate>
}
