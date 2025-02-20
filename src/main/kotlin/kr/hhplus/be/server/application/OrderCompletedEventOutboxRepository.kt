package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxFactory
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxJpaRepository
import org.springframework.stereotype.Repository

@Repository
class OrderCompletedEventOutboxRepository (
    private val repository: EventOutboxJpaRepository
) {
    fun save(order: Order) {
        repository.save(
            EventOutboxFactory.create(eventName="order-completed", data = order)
        )
    }
}