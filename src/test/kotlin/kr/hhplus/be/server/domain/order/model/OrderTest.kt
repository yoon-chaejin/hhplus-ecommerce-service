package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderTest {

    @Test
    fun `given 상품 목록과 쿠폰으로 when 주문 생성 시 then 주문 금액과 결제 금액이 계산되고, 쿠폰이 사용처리된다`() {
        //given
        val userId = 1L
        val discountRate = 10
        val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
        val template = CouponTemplate(
            id = 1L,
            discountRate = discountRate,
            issueCount = 1,
            maxIssueCount = 10,
            issuableUntil = now.plusDays(1),
        )
        val coupon = IssuedCoupon(
            id = 1L,
            template = template,
            ownedBy = userId,
            usedAt = null,
            expiresAt = now.plusDays(1),
            createdAt = now.minusDays(1),
            updatedAt = now.minusDays(1),
        )
        val orderProducts = listOf(
            OrderProduct(
                id = 1L,
                unitPrice = 500,
                quantity = 5,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        //when
        val result = Order(
            id = 1,
            coupon = coupon,
            orderProducts = orderProducts,
            orderedBy = userId
        )

        //then
        assertEquals(orderProducts.sumOf { it.unitPrice * it.quantity }, result.totalPrice)
        assertEquals(result.totalPrice * discountRate / 100, result.paymentPrice)
    }

    @Test
    fun `given 상품 목록으로 when 주문 생성 시 then 주문 금액과 결제 금액이 동일하다`() {
        //given
        val userId = 1L
        val coupon = null
        val orderProducts = listOf(
            OrderProduct(
                id = 1L,
                unitPrice = 500,
                quantity = 5,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        //when
        val result = Order(
            id = 1,
            coupon = coupon,
            orderProducts = orderProducts,
            orderedBy = userId
        )
        //then
        assertEquals(result.totalPrice, result.paymentPrice)
    }
}