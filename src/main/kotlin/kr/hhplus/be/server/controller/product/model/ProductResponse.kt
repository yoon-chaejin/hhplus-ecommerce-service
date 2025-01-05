package kr.hhplus.be.server.controller.product.model

import kr.hhplus.be.server.domain.product.model.ProductStatus

data class ProductResponse(
    val id: Long,
    val name: String,
    val unitPrice: Int,
    val remainingQuantity: Int,
    val status: ProductStatus,
)