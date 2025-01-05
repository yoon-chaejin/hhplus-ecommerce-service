package kr.hhplus.be.server.controller.point.model

import java.time.LocalDateTime

data class GetPointsResponse(
    val balance: Int,
    val updatedAt: LocalDateTime,
)
