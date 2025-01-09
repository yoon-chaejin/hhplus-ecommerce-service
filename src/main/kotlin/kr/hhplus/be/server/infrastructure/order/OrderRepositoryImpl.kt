package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.OrderRepository
import kr.hhplus.be.server.domain.order.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl @Autowired constructor(
    private val orderJpaRepository: OrderJpaRepository,
) : OrderRepository {
    override fun save(order: Order): Order {
        return orderJpaRepository.save(order)
    }
}