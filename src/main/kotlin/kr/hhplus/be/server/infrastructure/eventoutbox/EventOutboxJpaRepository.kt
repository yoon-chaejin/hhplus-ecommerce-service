package kr.hhplus.be.server.infrastructure.eventoutbox

import org.springframework.data.jpa.repository.JpaRepository

interface EventOutboxJpaRepository : JpaRepository<EventOutbox, Long> {
    fun findByMessage(message: String): EventOutbox?

    fun findAllByStatus(status: EventOutboxStatus): List<EventOutbox>
}