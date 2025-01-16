package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon

interface IssuedCouponRepository {
    fun findIssuedCouponByIdWithLock(id: Long): IssuedCoupon?
    fun findIssuedCouponsByUserId(userId: Long): List<IssuedCoupon>
    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
}