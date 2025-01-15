package kr.hhplus.be.server.controller.point.model

import kr.hhplus.be.server.point.model.Point
import java.time.LocalDateTime

data class ChargeResponse(
    val balance: Int,
    val updatedAt: LocalDateTime,
)

fun Point.toChargeResponse(): ChargeResponse {
    return ChargeResponse(
        balance = this.balance,
        updatedAt = this.updatedAt,
    )
}