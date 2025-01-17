package kr.hhplus.be.server.application

import kr.hhplus.be.server.application.model.PopularProductInfo
import kr.hhplus.be.server.domain.order.OrderProductService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductApplication @Autowired constructor (
    val productService: ProductService,
    val orderProductService: OrderProductService
) {
    fun getProducts(page: Pageable, status: ProductStatus?) : List<Product> {
        return if (status == null) {
            productService.getProducts(page)
        } else {
            productService.getProducts(page, status)
        }
    }

    fun getPopularProducts() : List<PopularProductInfo> {
        val end = LocalDateTime.now()
        val start = end.minusDays(3)

        return orderProductService.getPopularOrderProducts(start, end).map {
            PopularProductInfo(
                product = productService.getProductById(it.productId),
                cumulativeSalesQuantity = it.productQuantity
            )
        }
    }
}