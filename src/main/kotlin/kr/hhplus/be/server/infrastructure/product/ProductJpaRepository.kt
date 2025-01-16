package kr.hhplus.be.server.infrastructure.product

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface ProductJpaRepository : JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): Product?

    fun findProductsByRemainingQuantityGreaterThan(num: Int, page: Pageable): Page<Product>
    fun findProductsByRemainingQuantityLessThanEqual(num: Int, page: Pageable): Page<Product>
}