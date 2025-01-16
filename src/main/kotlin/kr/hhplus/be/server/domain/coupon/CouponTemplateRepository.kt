package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate

interface CouponTemplateRepository {
    fun findCouponTemplateByIdWithLock(id: Long): CouponTemplate?
}