package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponRepositoryImpl @Autowired constructor(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository {
    override fun findIssuedCouponByIdWithLock(id: Long): IssuedCoupon? {
        return issuedCouponJpaRepository.findForUpdateById(id)
    }

    override fun findIssuedCouponsByUserId(userId: Long): List<IssuedCoupon> {
        return issuedCouponJpaRepository.findIssuedCouponsByOwnedBy(userId)
    }

    override fun save(issuedCoupon: IssuedCoupon): IssuedCoupon {
        return issuedCouponJpaRepository.save(issuedCoupon)
    }
}