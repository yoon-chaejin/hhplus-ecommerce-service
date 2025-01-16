package kr.hhplus.be.server.controller.order.model

import kr.hhplus.be.server.domain.order.model.OrderProduct

data class OrderProductResponse(
    val id: Long,
    val unitPrice: Int,
    val quantity: Int,
)

fun OrderProduct.toOrderProductResponse() = OrderProductResponse(
    id = this.id,
    unitPrice = this.unitPrice,
    quantity = this.quantity,
)