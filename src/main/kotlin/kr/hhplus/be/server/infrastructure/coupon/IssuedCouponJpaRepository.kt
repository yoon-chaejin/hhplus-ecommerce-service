package kr.hhplus.be.server.infrastructure.coupon

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface IssuedCouponJpaRepository : JpaRepository<IssuedCoupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): IssuedCoupon?
}