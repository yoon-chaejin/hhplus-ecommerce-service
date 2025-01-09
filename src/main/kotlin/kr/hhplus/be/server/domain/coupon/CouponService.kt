package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponService @Autowired constructor (
    private val couponTemplateRepository: CouponTemplateRepository,
    private val issuedCouponRepository: IssuedCouponRepository
) {

    @Transactional
    fun issue(templateId: Long, userId: Long): IssuedCoupon {
        val couponTemplate = couponTemplateRepository.findCouponTemplateByIdWithLock(templateId) ?: throw CustomException(
            CustomExceptionType.COUPON_TEMPLATE_NOT_FOUND)

        val at = LocalDateTime.now()
        val issuedCoupon = couponTemplate.issueCoupon(userId, at)

        return issuedCouponRepository.save(issuedCoupon)
    }

    @Transactional
    fun use(couponId: Long, userId: Long) : IssuedCoupon {
        val coupon = issuedCouponRepository.findIssuedCouponByIdWithLock(couponId) ?: throw CustomException(CustomExceptionType.INVALID_COUPON)

        val at = LocalDateTime.now()
        return coupon.use(userId, at)
    }
}