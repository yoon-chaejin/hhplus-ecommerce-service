package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository {
    fun findProducts(page: Pageable): Page<Product>
    fun findPopularProducts(): List<Product>
    fun findProductByIdWithLock(id: Long): Product?
}
