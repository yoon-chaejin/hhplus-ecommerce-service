package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutbox
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxFactory
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxJpaRepository
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxStatus
import org.springframework.stereotype.Repository

@Repository
class OrderCompletedEventOutboxRepository (
    private val repository: EventOutboxJpaRepository
) {
    fun save(order: Order) {
        repository.save(
            EventOutboxFactory.create(eventName="order-completed", data = order, aggregateId = order.id)
        )
    }

    fun save(event: EventOutbox) {
        repository.save(event)
    }

    fun findEventOutboxByData(data: String): EventOutbox? {
        return repository.findByMessage(data)
    }

    fun findAllByStatus(status: EventOutboxStatus): List<EventOutbox> {
        return repository.findAllByStatus(status)
    }
}