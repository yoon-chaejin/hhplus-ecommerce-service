package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponRepositoryImpl @Autowired constructor(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository {
    override fun findIssuedCouponById(id: Long): IssuedCoupon? {
        return issuedCouponJpaRepository.findByIdOrNull(id)
    }

    override fun save(issuedCoupon: IssuedCoupon): IssuedCoupon {
        return issuedCouponJpaRepository.save(issuedCoupon)
    }
}