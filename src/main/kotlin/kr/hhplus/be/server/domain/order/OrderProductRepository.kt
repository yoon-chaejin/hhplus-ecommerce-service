package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import java.time.LocalDateTime

interface OrderProductRepository {
    fun findPopularOrderProductQuantitySumInfo(start: LocalDateTime, end: LocalDateTime): List<OrderProductQuantitySumInfo>
}