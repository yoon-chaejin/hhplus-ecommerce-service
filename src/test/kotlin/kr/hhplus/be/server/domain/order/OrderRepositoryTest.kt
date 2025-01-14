package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.TestcontainersConfiguration
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.infrastructure.order.OrderRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class, OrderRepositoryImpl::class)
class OrderRepositoryTest @Autowired constructor(
    val sut : OrderRepository,
) {
    @Test
    fun `given 주문 정보 when 주문 저장 시 then 주문 상품 목록도 저장된다`() {
        //given
        val order = Order(
            id = 0L,
            orderProducts = listOf(
                OrderProduct(
                    id = 0L,
                    quantity = 1,
                    unitPrice = 100,
                )
            ),
            coupon = null,
            orderedBy = 1L
        )

        //when
        val result = sut.save(order)

        //then
        assertTrue(result.orderProducts.isNotEmpty())

    }
}