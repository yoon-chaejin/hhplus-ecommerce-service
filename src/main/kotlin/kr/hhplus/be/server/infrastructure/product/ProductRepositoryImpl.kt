package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl @Autowired constructor(
    val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun findProducts(): List<Product> {
        return productJpaRepository.findAll()
    }

    override fun findPopularProducts(): List<Product> {
        return productJpaRepository.findAll()
    }

    override fun findProductById(id: Long): Product? {
        return productJpaRepository.findByIdOrNull(id)
    }
}