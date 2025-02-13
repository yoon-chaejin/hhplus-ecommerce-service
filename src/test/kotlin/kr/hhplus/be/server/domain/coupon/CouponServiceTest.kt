package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.junit.jupiter.api.assertInstanceOf
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CouponServiceTest {

    private val couponTemplateRepository: CouponTemplateRepository = mock<CouponTemplateRepository>()
    private val issuedCouponRepository: IssuedCouponRepository = mock<IssuedCouponRepository>()
    private val issueRequestRepository: IssueRequestRepository = mock<IssueRequestRepository>()

    private val sut: CouponService = CouponService(couponTemplateRepository, issuedCouponRepository, issueRequestRepository)

    @Test
    fun `쿠폰 목록이 없는 사용자인 경우, 쿠폰 목록 조회 시, 빈 리스트를 반환한다`() {
        //given
        val userId = 1L
        given(issuedCouponRepository.findIssuedCouponsByUserId(userId)).willReturn(emptyList())

        //when
        val result = sut.getIssuedCouponsByUserId(userId)

        //then
        assertTrue { result.isEmpty() }
    }

    @Test
    fun `쿠폰 목록이 있는 사용자인 경우, 쿠폰 목록 조회 시, 쿠폰 목록을 반환한다`() {
        //given
        val userId = 1L
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)

        val coupon = IssuedCoupon(
            id = 1L,
            discountRate = 10,
            couponTemplateRefKey = 1L,
            ownedBy = userId,
            usedAt = now,
            expiresAt = now.plusDays(1),
        )
        val coupons = listOf(coupon)

        given(issuedCouponRepository.findIssuedCouponsByUserId(userId)).willReturn(coupons)

        //when
        val result = sut.getIssuedCouponsByUserId(userId)

        //then
        assertEquals(coupons, result)
    }

    @Test
    fun `존재하지 않는 쿠폰 템플릿 id인 경우, 쿠폰 발급 시, CustomException이 발생한다`() {
        //given
        val templateId = 0L
        val userId = 1L
        given(couponTemplateRepository.findCouponTemplateByIdWithLock(templateId)).willReturn(null)

        //when
        val result = assertFailsWith<CustomException> {
            sut.issue(templateId, userId)
        }

        //then
        assertEquals(CustomExceptionType.COUPON_TEMPLATE_NOT_FOUND, result.type)
    }

    @Test
    fun `쿠폰 발급 시, 쿠폰이 발급된다`() {
        //given
        val templateId = 1L
        val userId = 1L
        val now = LocalDateTime.now()

        val couponTemplate = CouponTemplate(
            id = templateId,
            issueCount = 0,
            maxIssueCount = 10,
            discountRate = 10,
            issuableUntil = now.plusDays(1),
        )
        val issuedCoupon = couponTemplate.issueCoupon(userId, now)

        given(couponTemplateRepository.findCouponTemplateByIdWithLock(templateId)).willReturn(
            CouponTemplate(
                id = templateId,
                issueCount = 0,
                maxIssueCount = 10,
                discountRate = 10,
                issuableUntil = now.plusDays(1),
            )
        )

        given(issuedCouponRepository.save(any())).willReturn(issuedCoupon)

        //when
        val result = sut.issue(templateId, userId)

        //then
        assertInstanceOf<IssuedCoupon>(result)
        assertEquals(templateId, result.couponTemplateRefKey)
        assertEquals(userId, result.ownedBy)
    }

    @Test
    fun `존재하지 않는 쿠폰 id인 경우, 쿠폰 사용 시, CustomException이 발생한다`() {
        //given
        val couponId = 0L
        val userId = 1L
        given(issuedCouponRepository.findIssuedCouponByIdWithLock(couponId)).willReturn(null)

        //when
        val result = assertFailsWith<CustomException> {
            sut.use(couponId, userId)
        }

        //then
        assertEquals(CustomExceptionType.INVALID_COUPON, result.type)
    }
}