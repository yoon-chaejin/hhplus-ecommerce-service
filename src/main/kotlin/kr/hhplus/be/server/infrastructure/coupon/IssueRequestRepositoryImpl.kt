package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.IssueRequestRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class IssueRequestRepositoryImpl (
    redisTemplate: RedisTemplate<String, String>
) : IssueRequestRepository {
    private val operations = redisTemplate.opsForZSet()

    override fun saveRequest(key: String, value: String, score: Double) {
        operations.addIfAbsent(key, value, score)
    }

    override fun findRequests(key: String, count: Long): List<String> {
        return operations.reverseRangeByScore(key, 0.0, Double.MAX_VALUE, 0, count)!!.toList()
    }
}