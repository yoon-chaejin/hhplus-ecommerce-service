package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<Product, Long> {

}
