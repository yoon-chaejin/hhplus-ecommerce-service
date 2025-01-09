package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.Order
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository {
    fun save(order: Order): Order
}