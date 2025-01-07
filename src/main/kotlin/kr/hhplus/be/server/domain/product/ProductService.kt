package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor (
    private val productRepository: ProductRepository
) {

    fun getProducts() : List<Product> {
        return productRepository.findProducts()
    }

    fun getPopularProducts() : List<Product> {
        return productRepository.findPopularProducts()
    }

    fun decreaseProductQuantity(productId: Long, orderQuantity: Int): Product {
        val product = productRepository.findProductById(productId) ?: throw CustomException(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND)
        return product.decreaseQuantityBy(orderQuantity)
    }
}