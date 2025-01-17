package kr.hhplus.be.server.controller.product.model

import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus

data class ProductResponse(
    val id: Long,
    val name: String,
    val unitPrice: Int,
    val remainingQuantity: Int,
    val status: ProductStatus,
)

fun Product.toProductResponse(): ProductResponse {
    return ProductResponse(
        id = this.id,
        name = this.name,
        unitPrice = this.unitPrice,
        remainingQuantity = this.remainingQuantity,
        status = this.getStatus()
    )
}