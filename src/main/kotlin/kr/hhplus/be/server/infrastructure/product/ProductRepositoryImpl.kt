package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl @Autowired constructor(
    val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun findProducts(page: Pageable): Page<Product> {
        return productJpaRepository.findAll(page)
    }

    override fun findProductsByRemainingQuantityGreaterThan(num: Int, page: Pageable): Page<Product> {
        return productJpaRepository.findProductsByRemainingQuantityGreaterThan(num, page)
    }

    override fun findProductsByRemainingQuantityLessThanEqual(num: Int, page: Pageable): Page<Product> {
        return productJpaRepository.findProductsByRemainingQuantityLessThanEqual(num, page)
    }

    override fun findPopularProducts(): List<Product> {
        return productJpaRepository.findAll()
    }

    override fun findProductById(id: Long): Product? {
        return productJpaRepository.findByIdOrNull(id)
    }

    override fun findProductByIdWithLock(id: Long): Product? {
        return productJpaRepository.findForUpdateById(id)
    }
}