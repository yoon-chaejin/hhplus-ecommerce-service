package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findProducts(page: Pageable): Page<Product>
    fun findProductsByRemainingQuantityGreaterThan(num: Int, page: Pageable): Page<Product>
    fun findProductsByRemainingQuantityLessThanEqual(num: Int, page: Pageable): Page<Product>
    fun findPopularProducts(): List<Product>
    fun findProductByIdWithLock(id: Long): Product?
}
