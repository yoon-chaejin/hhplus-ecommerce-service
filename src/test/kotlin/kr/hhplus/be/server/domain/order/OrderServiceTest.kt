package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderProduct
import org.junit.jupiter.api.assertInstanceOf
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import kotlin.test.Test

class OrderServiceTest {
    private val orderRepository: OrderRepository = mock<OrderRepository>()
    private val sut = OrderService(orderRepository)

    @Test
    fun `given when 주문 생성 시 then 주문이 반환된다`() {
        //given
        val userId = 1L
        val coupon = null
        val products = listOf(
            OrderProduct(
                id = 1L,
                unitPrice = 500,
                quantity = 5,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )
        given(orderRepository.save(order = any())).willReturn(
            Order(orderedBy = userId, coupon = coupon, orderProducts = products)
        )

        //when
        val result = sut.create(userId, products, coupon)

        //then
        assertInstanceOf<Order>(result)
    }
}