package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.CouponTemplateRepository
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateRepositoryImpl @Autowired constructor (
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository
): CouponTemplateRepository {
    override fun findCouponTemplateById(id: Long): CouponTemplate? {
        return couponTemplateJpaRepository.findByIdOrNull(id)
    }
}