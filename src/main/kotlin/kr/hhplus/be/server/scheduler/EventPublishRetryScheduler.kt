package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.application.OrderCompletedEventKafkaPublisher
import kr.hhplus.be.server.application.OrderCompletedEventOutboxRepository
import kr.hhplus.be.server.infrastructure.eventoutbox.EventOutboxStatus
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
@Profile("local", "scheduler")
class EventPublishRetryScheduler (
    private val eventOutboxRepository: OrderCompletedEventOutboxRepository,
    private val kafkaPublisher: OrderCompletedEventKafkaPublisher
){
    @Scheduled(fixedRate = 5000)
    fun retryEventPublish() {
        eventOutboxRepository.findAllByStatus(EventOutboxStatus.INIT).forEach {
            if (it.createdAt < LocalDateTime.now().minusMinutes(5)) {
                kafkaPublisher.publish(it.message)
            }
        }
    }
}