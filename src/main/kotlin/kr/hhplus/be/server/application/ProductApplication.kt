package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductApplication @Autowired constructor (
    val productService: ProductService
) {
    fun getProducts(page: Pageable, status: ProductStatus?) : List<Product> {
        return if (status == null) {
            productService.getProducts(page)
        } else {
            productService.getProducts(page, status)
        }
    }
}