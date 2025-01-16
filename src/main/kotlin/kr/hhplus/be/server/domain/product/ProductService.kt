package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService @Autowired constructor (
    private val productRepository: ProductRepository
) {

    fun getProducts(page: Pageable) : List<Product> {
        return productRepository.findProducts(page).content
    }

    fun getProducts(page: Pageable, status: ProductStatus) : List<Product> {
        return when (status) {
            ProductStatus.AVAILABLE -> productRepository.findProductsByRemainingQuantityGreaterThan(0, page)
            ProductStatus.UNAVAILABLE -> productRepository.findProductsByRemainingQuantityLessThanEqual(0, page)
        }.content
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