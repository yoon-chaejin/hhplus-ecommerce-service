package kr.hhplus.be.server.application

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.model.PopularProductInfo
import kr.hhplus.be.server.common.utils.LocalDateTimeTruncator
import kr.hhplus.be.server.domain.order.OrderProductService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductApplication @Autowired constructor (
    val productService: ProductService,
    val orderProductService: OrderProductService,
    val redisTemplate: RedisTemplate<String, String>,
    val objectMapper: ObjectMapper
) {

    fun getProducts(page: Pageable, status: ProductStatus?) : List<Product> {
        return if (status == null) {
            productService.getProducts(page)
        } else {
            productService.getProducts(page, status)
        }
    }

    fun getPopularProductsFromDatabase() : List<PopularProductInfo> {
        val end = LocalDateTimeTruncator.truncateToNearestFiveMinutes(LocalDateTime.now())
        val start = end.minusDays(3)

        return orderProductService.getPopularOrderProducts(start, end).map {
            PopularProductInfo(
                product = productService.getProductById(it.productId),
                cumulativeSalesQuantity = it.productQuantity
            )
        }
    }

    fun getPopularProducts(): List<PopularProductInfo> {
        return runCatching {
            getPopularProductsFromCache()
        }.getOrElse {
            getPopularProductsFromDatabase()
        }.also {
            setPopularProductsToCache(it)
        }
    }

    fun getPopularProductsFromCache() : List<PopularProductInfo> {
        val operation = redisTemplate.opsForValue()
        val json: String = operation["popularProducts"] ?: throw NoSuchElementException()
        val data: List<PopularProductInfo> = objectMapper.readValue(json, Array<PopularProductInfo>::class.java).toList()

        return data
    }

    fun setPopularProductsToCache(popularProducts: List<PopularProductInfo>) {
        val operation = redisTemplate.opsForValue()
        val json = objectMapper.writeValueAsString(popularProducts)
        operation["popularProducts"] = json
    }
}