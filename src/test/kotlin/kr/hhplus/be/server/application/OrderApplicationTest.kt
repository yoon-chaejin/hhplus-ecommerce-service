package kr.hhplus.be.server.application

import kr.hhplus.be.server.controller.order.model.OrderProductRequest
import kr.hhplus.be.server.controller.order.model.OrderRequest
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.point.model.Point
import org.mockito.ArgumentMatchers.*
import org.mockito.kotlin.*
import org.mockito.kotlin.any
import java.time.LocalDateTime
import kotlin.test.Test

class OrderApplicationTest {
    private val orderService = mock<OrderService>()
    private val productService = mock<ProductService>()
    private val couponService = mock<CouponService>()
    private val pointService = mock<PointService>()

    private val sut = OrderApplication(orderService, productService, couponService, pointService)

    @Test
    fun `쿠폰이 있을 경우, 주문 시, 쿠폰 사용이 호출된다`() {
        //given
        val userId = 1L
        val couponId = 1L
        val orderRequest = OrderRequest(
            products = listOf(
                OrderProductRequest(1L, 1)
            ),
            couponId
        )
        val now = LocalDateTime.of(2025, 2, 1, 0, 0, 0)

        val product = Product(
            id = 1L,
            name = "상품명",
            remainingQuantity = 0,
            unitPrice = 100,
        )
        val coupon = IssuedCoupon(
            id = 1L,
            couponTemplateRefKey = 1L,
            discountRate = 10,
            ownedBy = 1L,
            usedAt = now,
            expiresAt = now.plusDays(365)
        )
        given(productService.decreaseProductQuantity(anyLong(), anyInt())).willReturn(product)
        given(couponService.use(anyLong(), anyLong())).willReturn(coupon)
        given(orderService.create(anyLong(), any(), any())).willReturn(
            Order(
                id = 1L,
                orderProducts = listOf(
                    OrderProduct(
                        id = 1L,
                        unitPrice = 100,
                        quantity = 1,
                        productId = 1L,
                    )
                ),
                coupon = coupon,
                orderedBy = userId
            )
        )
        given(pointService.use(anyLong(), anyInt())).willReturn(
            Point(
                id = userId,
                userId = userId,
            ))

        //when
        val result = sut.order(userId, orderRequest)

        //then
        verify(couponService).use(anyLong(), anyLong())
    }
}