package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderProductJpaRepository : JpaRepository<OrderProduct, Long> {
    @Query("SELECT new kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo(op.productId, SUM(op.quantity)) " +
            "FROM OrderProduct op " +
            "WHERE op.createdAt BETWEEN :start AND :end " +
            "GROUP BY op.productId " +
            "ORDER BY SUM(op.quantity) DESC " +
            "LIMIT 5")
    fun findPopularOrderProductQuantitySumInfo(start: LocalDateTime, end: LocalDateTime): List<OrderProductQuantitySumInfo>
}