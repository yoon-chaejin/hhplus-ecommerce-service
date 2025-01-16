package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.Order

interface OrderRepository {
    fun save(order: Order): Order
}