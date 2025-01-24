package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import org.junit.jupiter.api.assertInstanceOf
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CouponTemplateTest {
    @Test
    fun `쿠폰 템플릿의 발급 개수가 최대 발급 개수와 같은 경우, 쿠폰 발급 시, CustomException이 발생한다` () {
        //given
        val userId = 1L
        val num = 10

        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val issuableUntil = now.plusDays(1)
        val createdAt = now.minusDays(1)

        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = num,
            maxIssueCount = num,
            issuableUntil = issuableUntil,
        )

        //when
        val result = assertFailsWith<CustomException> {
            template.issueCoupon(userId, now)
        }

        //then
        assertEquals(CustomExceptionType.COUPON_ISSUE_FAILED, result.type)
    }

    @Test
    fun `쿠폰 템플릿의 발급 기한이 지난 경우, 쿠폰 발급 시, CustomException이 발생한다`() {
        //given
        val userId = 1L
        val num = 10

        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val issuableUntil = now.minusDays(1)
        val createdAt = now.minusDays(2)

        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = num - 1,
            maxIssueCount = num,
            issuableUntil = issuableUntil,
        )

        //when
        val result = assertFailsWith<CustomException> {
            template.issueCoupon(userId, now)
        }

        //then
        assertEquals(CustomExceptionType.COUPON_ISSUE_FAILED, result.type)
    }

    @Test
    fun `쿠폰 발급 시, 365일 이후 만료되는 쿠폰이 발급된다`() {
        //given
        val userId = 1L
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)

        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )

        //when
        val result = template.issueCoupon(userId, now)

        //then
        assertInstanceOf<IssuedCoupon>(result)
        assertEquals(userId, result.ownedBy)
        assertEquals(now.plusDays(365), result.expiresAt)
    }
}