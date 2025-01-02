package kr.hhplus.be.server.controller.order.model

data class OrderProductResponse(
    val id: Long,
    val name: String,
    val unitPrice: Int,
    val quantity: Int,
)
