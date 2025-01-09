package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateJpaRepository: JpaRepository<CouponTemplate, Long> {

}
