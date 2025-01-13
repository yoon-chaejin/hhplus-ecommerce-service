package kr.hhplus.be.server.domain.coupon.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IssuedCouponTest {
    @Test
    fun `given 사용일시가 존재하는 경우 when 상태 조회 시 then 사용 완료로 반환하다`() {
        //given
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )

        val coupon = IssuedCoupon(
            id = 1L,
            template = template,
            ownedBy = 1L,
            usedAt = now,
            expiresAt = now.plusDays(1),
            createdAt = now.minusDays(1),
            updatedAt = now.minusDays(1),
        )

        //when
        val result = coupon.getStatus(now)

        //then
        assertEquals(CouponStatus.USED, result)
    }

    @Test
    fun `given 사용일시가 없고, 사용기한이 지난 경우 when 상태 조회 시 기한 만료를 반환한다`() {
        //given
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )

        val coupon = IssuedCoupon(
            id = 1L,
            template = template,
            ownedBy = 1L,
            usedAt = null,
            expiresAt = now.minusDays(1),
            createdAt = now.minusDays(2),
            updatedAt = now.minusDays(2),
        )

        //when
        val result = coupon.getStatus(now)

        //then
        assertEquals(CouponStatus.EXPIRED, result)
    }

    @Test
    fun `given 사용일시가 없고, 사용기간이 지나지 않은 경우 when 상태 조회 시 then 사용 가능을 반환한다`() {
        //given
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )

        val coupon = IssuedCoupon(
            id = 1L,
            template = template,
            ownedBy = 1L,
            usedAt = null,
            expiresAt = now.plusDays(1),
            createdAt = now.minusDays(2),
            updatedAt = now.minusDays(2),
        )

        //when
        val result = coupon.getStatus(now)

        //then
        assertEquals(CouponStatus.USABLE, result)
    }

    @Test
    fun `given 주문자와 쿠폰 소유자가 일치하지 않는 경우 when 쿠폰 사용 시 then CustomException을 반환한다`() {
        //given
        val ownerId = 1L
        val userId = 2L
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val template = CouponTemplate(
            id = 1L,
            discountRate = 10,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )

        val coupon = IssuedCoupon(
            id = 1L,
            template = template,
            ownedBy = ownerId,
            usedAt = null,
            expiresAt = now.plusDays(1),
            createdAt = now.minusDays(1),
            updatedAt = now.minusDays(1),
        )

        //when
        val result = assertFailsWith<CustomException> {
            coupon.use(userId, now)
        }

        //then
        assertEquals(CustomExceptionType.INVALID_COUPON, result.type)
    }

}