package kr.hhplus.be.server.domain.product.model

import jakarta.persistence.*
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.common.model.BaseEntity
import java.lang.IllegalArgumentException

@Entity
@Table(name = "product")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    var remainingQuantity: Int,
    val unitPrice: Int,
) : BaseEntity() {
    init {
        require(unitPrice % 100 == 0) { throw IllegalArgumentException("상품 가격은 100 단위로 지정해야 합니다.")}
    }

    fun getStatus(): ProductStatus {
        return if (remainingQuantity > 0) ProductStatus.AVAILABLE
        else ProductStatus.UNAVAILABLE
    }

    fun decreaseQuantityBy(amount: Int): Product {
        require(remainingQuantity >= amount) { throw CustomException(CustomExceptionType.NOT_ENOUGH_QUANTITY) }
        remainingQuantity -= amount
        return this
    }
}