package kr.hhplus.be.server.application.model

import kr.hhplus.be.server.domain.product.model.Product

data class PopularProductInfo (
    private val product: Product,
    private val cumulativeSalesQuantity: Long,
)
