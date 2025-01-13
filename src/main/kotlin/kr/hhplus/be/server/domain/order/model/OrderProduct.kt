package kr.hhplus.be.server.domain.order.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class OrderProduct(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val quantity: Int,
    val unitPrice: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
}