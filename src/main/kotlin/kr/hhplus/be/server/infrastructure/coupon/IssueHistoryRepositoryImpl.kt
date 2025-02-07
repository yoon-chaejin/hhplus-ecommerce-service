package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.IssueHistoryRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class IssueHistoryRepositoryImpl (
    redisTemplate: RedisTemplate<String, String>
) : IssueHistoryRepository {
    private val operations = redisTemplate.opsForSet()

    override fun findIssueHistoryByKey(key: String, value: String): Boolean {
        return operations.isMember(key, value) ?: throw NoSuchElementException()
    }

    override fun saveIssueHistory(key: String, value: String) {
        TODO("Not yet implemented")
    }

}