package kr.hhplus.be.server.application.model

import kr.hhplus.be.server.domain.product.model.Product

data class PopularProductInfo (
    val product: Product,
    val cumulativeSalesQuantity: Long,
)
