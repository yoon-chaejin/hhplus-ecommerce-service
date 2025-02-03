package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertInstanceOf
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CouponTemplateTests {

    @Nested
    @DisplayName("쿠폰 발급 테스트")
    inner class IssueCouponTests {
        @Test
        fun `(실패) 최대 발급 개수에 도달한 경우, CustomException이 발생한다` () {
            //given
            val userId = 1L
            val num = 10

            val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
            val issuableUntil = now.plusDays(1)

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
        fun `(실패) 발급 기한이 지난 경우, CustomException이 발생한다`() {
            //given
            val userId = 1L
            val num = 10

            val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
            val issuableUntil = now.minusDays(1)

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
        fun `(성공) 쿠폰이 발급된다`() {
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
}