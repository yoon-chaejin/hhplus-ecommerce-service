package kr.hhplus.be.server.infrastructure.coupon

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface CouponTemplateJpaRepository: JpaRepository<CouponTemplate, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): CouponTemplate?
}
