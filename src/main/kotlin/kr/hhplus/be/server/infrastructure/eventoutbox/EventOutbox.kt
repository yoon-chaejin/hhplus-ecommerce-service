package kr.hhplus.be.server.infrastructure.eventoutbox

import jakarta.persistence.*

@Entity
class EventOutbox (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L,
    private val eventName: String,
    private val eventKey: String? = null,
    private val status: EventOutboxStatus = EventOutboxStatus.INIT,
    @Column(columnDefinition = "TEXT")
    private val message: String,
)