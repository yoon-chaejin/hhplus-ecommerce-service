package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService @Autowired constructor (
    private val productRepository: ProductRepository
) {

    fun getProducts(page: Pageable) : Page<Product> {
        return productRepository.findProducts(page)
    }

    fun getPopularProducts() : List<Product> {
        return productRepository.findPopularProducts()
    }

    @Transactional
    fun decreaseProductQuantity(productId: Long, orderQuantity: Int): Product {
        val product = productRepository.findProductByIdWithLock(productId) ?: throw CustomException(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND)
        return product.decreaseQuantityBy(orderQuantity)
    }
}