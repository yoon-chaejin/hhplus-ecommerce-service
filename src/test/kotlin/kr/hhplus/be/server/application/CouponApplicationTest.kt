package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import org.mockito.kotlin.*
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.test.Test

class CouponApplicationTest {
    private val couponService = mock<CouponService>()
    private val sut = CouponApplication(couponService)

    @Test
    fun `쿠폰 발급 시, 발급 가능한 쿠폰을 조회해 발급 가능한 개수만큼 요청을 가져와서 발급한다, `() {
        //given
        val couponTemplates = listOf(
            CouponTemplate(
                id = 1L,
                discountRate = 10,
                issueCount = 0,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            ),
            CouponTemplate(
                id = 2L,
                discountRate = 10,
                issueCount = 5,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            ),
            CouponTemplate(
                id = 3L,
                discountRate = 10,
                issueCount = 9,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            )
        )
        given(couponService.getCouponTemplatesIssuable()).willReturn(couponTemplates)
        for (couponTemplate in couponTemplates) {
            val count = couponTemplate.maxIssueCount - couponTemplate.issueCount
            given(couponService.getCouponIssueRequests(
                eq(couponTemplate.id),
                eq(count)
            )).willReturn((1..count).map { Random.nextLong().toString() }.toList())
        }

        //when
        sut.issue()

        //then
        verify(couponService, times(couponTemplates.size)).getCouponIssueRequests(any(), any())
        for (couponTemplate in couponTemplates) {
            verify(couponService, times(couponTemplate.maxIssueCount - couponTemplate.issueCount)).issue(eq(couponTemplate.id), any())
        }
    }
}