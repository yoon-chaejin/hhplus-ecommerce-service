package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.OrderProductRepository
import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderProductRepositoryImpl @Autowired constructor(
    private val orderProductJpaRepository: OrderProductJpaRepository
) : OrderProductRepository {
    override fun findPopularOrderProductQuantitySumInfo(start: LocalDateTime, end: LocalDateTime): List<OrderProductQuantitySumInfo> {
        return orderProductJpaRepository.findPopularOrderProductQuantitySumInfo(start, end)
    }
}