package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderProductService @Autowired constructor(
    private val orderProductRepository: OrderProductRepository
) {
    fun getPopularOrderProducts(start: LocalDateTime, end: LocalDateTime): List<OrderProductQuantitySumInfo> {
        require(start <= end) { "검색 시작 시점은 검색 종료 시점보다 작거나 같아야 합니다." }
        return orderProductRepository.findPopularOrderProductQuantitySumInfo(start, end)
    }
}