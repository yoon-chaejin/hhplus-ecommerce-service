package kr.hhplus.be.server.infrastructure.eventoutbox

import jakarta.persistence.*
import kr.hhplus.be.server.common.model.BaseEntity

@Entity
class EventOutbox (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L,
    private val eventName: String,
    private val eventKey: String? = null,
    @Enumerated(EnumType.STRING)
    private var status: EventOutboxStatus = EventOutboxStatus.INIT,
    @Column(columnDefinition = "TEXT")
    val message: String,
    private val aggregateId: Long,
) : BaseEntity() {
    fun published(): EventOutbox {
        this.status = EventOutboxStatus.PUBLISHED
        return this
    }
}