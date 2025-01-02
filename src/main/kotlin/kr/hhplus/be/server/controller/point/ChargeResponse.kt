package kr.hhplus.be.server.controller.point

import java.time.LocalDateTime

data class ChargeResponse(
    val balance: Int,
    val updatedAt: LocalDateTime,
)
