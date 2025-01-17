package kr.hhplus.be.server.controller.product.model

import kr.hhplus.be.server.application.model.PopularProductInfo
import kr.hhplus.be.server.domain.product.model.ProductStatus

data class PopularProductResponse(
    val id: Long,
    val name: String,
    val unitPrice: Int,
    val remainingQuantity: Int,
    val status: ProductStatus,
    val cumulativeSaleQuantity: Long,
)

fun PopularProductInfo.toPopularProductResponse() = PopularProductResponse(
    id = this.product.id,
    name = this.product.name,
    unitPrice = this.product.unitPrice,
    remainingQuantity = this.product.remainingQuantity,
    status = this.product.getStatus(),
    cumulativeSaleQuantity = this.cumulativeSalesQuantity
)