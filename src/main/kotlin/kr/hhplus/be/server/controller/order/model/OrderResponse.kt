package kr.hhplus.be.server.controller.order.model

import kr.hhplus.be.server.domain.order.model.Order
import java.time.LocalDateTime

data class OrderResponse(
    val orderId: Long,
    val products: List<OrderProductResponse>,
    val totalPrice: Int,
    val paymentPrice: Int,
    val orderedAt: LocalDateTime,
)

fun Order.toOrderResponse() = OrderResponse(
    orderId = this.id,
    products = this.orderProducts.map { it.toOrderProductResponse()},
    totalPrice = this.totalPrice,
    paymentPrice = this.paymentPrice,
    orderedAt = this.createdAt
)
