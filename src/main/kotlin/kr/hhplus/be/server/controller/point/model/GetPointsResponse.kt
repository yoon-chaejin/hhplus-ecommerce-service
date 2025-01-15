package kr.hhplus.be.server.controller.point.model

import kr.hhplus.be.server.point.model.Point
import java.time.LocalDateTime

data class GetPointsResponse(
    val balance: Int,
    val updatedAt: LocalDateTime,
)

fun Point.toGetPointsResponse(): GetPointsResponse {
    return GetPointsResponse(
        balance = this.balance,
        updatedAt = this.updatedAt,
    )
}