package kr.hhplus.be.server.controller.product

import kr.hhplus.be.server.controller.product.model.GetPopularProductsResponse
import kr.hhplus.be.server.controller.product.model.GetProductsResponse
import kr.hhplus.be.server.controller.product.model.PopularProductResponse
import kr.hhplus.be.server.controller.product.model.ProductResponse
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController {

    @GetMapping("products")
    fun getProducts(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int,
        @RequestParam(value = "status") status: ProductStatus?
    ): ResponseEntity<GetProductsResponse> {
        val products = ArrayList<ProductResponse>()

        for (i in 0 until size) {
            products.add(
                ProductResponse(
                    id = (page*size + i).toLong(),
                    name = "상품명",
                    unitPrice = i * 100,
                    remainingQuantity = 50,
                    status = status ?: ProductStatus.AVAILABLE
                )
            )
        }

        return ResponseEntity.ok(GetProductsResponse(products))
    }

    @GetMapping("/products/popular")
    fun getPopularProducts(): ResponseEntity<GetPopularProductsResponse> {
        val products = ArrayList<PopularProductResponse>()

        for (i in 0 until 5) {
            products.add(
                PopularProductResponse(
                    id = (0 .. 100).random().toLong(),
                    name = "상품명",
                    unitPrice = i * 100,
                    remainingQuantity = 50,
                    status = ProductStatus.AVAILABLE,
                    cumulativeSaleQuantity = 250,
                )
            )
        }

        return ResponseEntity.ok(GetPopularProductsResponse(products))
    }
}