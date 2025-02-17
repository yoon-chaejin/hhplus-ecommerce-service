package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.model.Order
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendDataPlatform(order: Order) {
        DataPlatform.send(order)
    }
}