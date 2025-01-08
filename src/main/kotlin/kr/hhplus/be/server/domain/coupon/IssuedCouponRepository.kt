package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.springframework.stereotype.Repository

@Repository
interface IssuedCouponRepository {
    fun findIssuedCouponById(id: Long): IssuedCoupon?
    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
}