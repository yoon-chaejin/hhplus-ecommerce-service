package kr.hhplus.be.server.controller.order.model

data class OrderRequest(
    val products: List<OrderProductRequest>,
    val couponId: Long?,
)
