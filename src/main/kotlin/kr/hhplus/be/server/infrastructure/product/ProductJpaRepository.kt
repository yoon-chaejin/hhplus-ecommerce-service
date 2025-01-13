package kr.hhplus.be.server.infrastructure.product

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface ProductJpaRepository : JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(id: Long): Product?
}