package kr.hhplus.be.server.infrastructure.eventoutbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.stereotype.Component

@Component
class EventOutboxFactory {
    companion object {
        private val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())  // LocalDateTime 지원 추가

        fun create(eventName: String, data: Any, aggregateId: Long): EventOutbox {
            return EventOutbox(
                eventName = eventName,
                message = objectMapper.writeValueAsString(data),
                aggregateId = aggregateId
            )
        }
    }
}