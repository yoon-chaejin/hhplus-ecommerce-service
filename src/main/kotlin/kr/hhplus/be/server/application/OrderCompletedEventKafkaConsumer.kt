package kr.hhplus.be.server.application

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderCompletedEventKafkaConsumer @Autowired constructor(
    private val eventOutboxRepository: OrderCompletedEventOutboxRepository
){
    @KafkaListener(topics = ["order-completed"], groupId = "update-outbox")
    fun consume(record: ConsumerRecord<String, String>) {
        val eventOutbox = eventOutboxRepository.findEventOutboxByData(record.value()) ?: throw NotFoundException()
        eventOutboxRepository.save(eventOutbox.published())
    }
}