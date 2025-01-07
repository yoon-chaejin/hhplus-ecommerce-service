package kr.hhplus.be.server.domain.product.model

import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class Product(
    val id: Long,
    val remainingQuantity: Int,
    val unitPrice: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(unitPrice % 100 == 0) { throw IllegalArgumentException("상품 가격은 100 단위로 지정해야 합니다.")}
    }

    fun getStatus(): ProductStatus {
        return if (remainingQuantity > 0) ProductStatus.AVAILABLE
        else ProductStatus.UNAVAILABLE
    }
}