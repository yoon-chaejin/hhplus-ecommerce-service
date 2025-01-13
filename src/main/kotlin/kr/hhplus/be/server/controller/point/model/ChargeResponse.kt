package kr.hhplus.be.server.controller.point.model

import java.time.LocalDateTime

data class ChargeResponse(
    val balance: Int,
    val updatedAt: LocalDateTime,
)
