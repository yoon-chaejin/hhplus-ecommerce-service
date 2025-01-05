package kr.hhplus.be.server.controller.order.model

import java.time.LocalDateTime

data class OrderResponse(
    val orderId: Long,
    val products: List<OrderProductResponse>,
    val totalPrice: Int,
    val paymentPrice: Int,
    val orderedAt: LocalDateTime,
)
