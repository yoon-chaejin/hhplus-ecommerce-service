package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.springframework.stereotype.Repository

@Repository
interface CouponTemplateRepository {
    fun findCouponTemplateById(id: Long): CouponTemplate?
}