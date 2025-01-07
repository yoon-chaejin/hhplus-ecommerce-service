package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository {
    fun findProducts(): List<Product>
    fun findPopularProducts(): List<Product>
    fun findProductById(id: Long): Product?
}
