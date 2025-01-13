package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService @Autowired constructor(
    val orderRepository: OrderRepository
) {
    fun create(userId: Long, products: List<OrderProduct>, coupon: IssuedCoupon?): Order {
        return orderRepository.save(
            Order(
                coupon = coupon,
                orderedBy = userId,
                orderProducts = products,
            )
        )
    }
}