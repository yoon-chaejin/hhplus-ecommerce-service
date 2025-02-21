package kr.hhplus.be.server.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import kr.hhplus.be.server.domain.order.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderCompletedEventKafkaPublisher @Autowired constructor (
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val objectMapper = ObjectMapper().registerModules(JavaTimeModule())

    fun publish(order: Order) {
        kafkaTemplate.send("order-completed", objectMapper.writeValueAsString(order))
    }
}