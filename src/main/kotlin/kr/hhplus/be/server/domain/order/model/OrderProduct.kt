package kr.hhplus.be.server.domain.order.model

import java.time.LocalDateTime

class OrderProduct(
    val id: Long,
    val quantity: Int,
    val unitPrice: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
}