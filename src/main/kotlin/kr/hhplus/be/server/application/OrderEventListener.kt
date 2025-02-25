package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderEventListener @Autowired constructor (
    private val eventOutboxRepository: OrderCompletedEventOutboxRepository,
    private val eventPublisher: OrderCompletedEventKafkaPublisher,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveEventToOutbox(order: Order) {
        eventOutboxRepository.save(order)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(order: Order) {
        eventPublisher.publish(order)
    }
}