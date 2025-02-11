package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrderTests {

    @Nested
    @DisplayName("주문 생성 테스트")
    inner class ConstructorTests {
        @Test
        fun `(실패) 주문 상품 목록이 빈 경우, IllegalArgumentException이 발생한다`() {
            //given
            val orderProducts = emptyList<OrderProduct>()
            val userId = 1L

            //when

            //then
            assertFailsWith<IllegalArgumentException> {
                Order(
                    orderProducts = orderProducts,
                    coupon = null,
                    orderedBy = userId,
                )
            }
        }

        @Test
        fun `(실패) 주문수량이 0인 경우, IllegalArgumentException이 발생한다`() {
            //given
            val orderProducts = listOf(
                OrderProduct(
                    id = 1L,
                    productId = 1L,
                    quantity = 0,
                    unitPrice = 100,
                )
            )
            val userId = 1L

            //when

            //then
            assertFailsWith<IllegalArgumentException> {
                Order(
                    orderProducts = orderProducts,
                    coupon = null,
                    orderedBy = userId,
                )
            }

        }

        @Test
        fun `(실패) 쿠폰 소유자와 주문자가 다른 경우, IllegalArgumentException이 발생한다`() {
            //given
            val orderProducts = listOf(
                OrderProduct(
                    id = 1,
                    productId = 1L,
                    quantity = 1,
                    unitPrice = 100,
                )
            )
            val userId = 1L
            val ownerId = 2L
            val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)

            val issuedCoupon = IssuedCoupon(
                id = 1L,
                ownedBy = ownerId,
                discountRate = 10,
                couponTemplateRefKey = 1L,
                usedAt = null,
                expiresAt = now.minusDays(1),
            )

            //when

            //then
            assertFailsWith<IllegalArgumentException> {
                Order(
                    orderProducts = orderProducts,
                    coupon = issuedCoupon,
                    orderedBy = userId,
                )
            }

        }

        @Test
        fun `(성공) 상품 목록과 쿠폰이 주어진 경우, 주문 금액과 결제 금액이 계산되고 쿠폰이 사용처리된다`() {
            //given
            val userId = 1L
            val discountRate = 10
            val now = LocalDateTime.of(2025, 1, 8, 1, 0, 0)
            
            val coupon = IssuedCoupon(
                id = 1L,
                discountRate = discountRate,
                couponTemplateRefKey = 1L,
                ownedBy = userId,
                usedAt = null,
                expiresAt = now.plusDays(1),
            )
            val orderProducts = listOf(
                OrderProduct(
                    id = 1L,
                    unitPrice = 500,
                    quantity = 5,
                    productId = 1L,
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
        fun `(성공) 상품 목록이 주어진 경우, 주문 금액과 결제 금액이 동일하다`() {
            //given
            val userId = 1L
            val coupon = null
            val orderProducts = listOf(
                OrderProduct(
                    id = 1L,
                    unitPrice = 500,
                    quantity = 5,
                    productId = 1L,
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
}