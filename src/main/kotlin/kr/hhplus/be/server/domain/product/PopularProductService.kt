package kr.hhplus.be.server.domain.product

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.model.PopularProductInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class PopularProductService @Autowired constructor (
    val redisTemplate: RedisTemplate<String, String>,
    val objectMapper: ObjectMapper
) {
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