package kr.hhplus.be.server.controller.point

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.point.model.ChargeRequest
import kr.hhplus.be.server.controller.point.model.GetPointsResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class PointController {

    @PostMapping("/users/{userId}/points/charge")
    fun charge(@PathVariable userId: Long, @RequestBody request: ChargeRequest): ResponseEntity<ChargeResponse> {
        require(request.amount > 0) { throw CustomException(CustomExceptionType.INVALID_CHARGE_AMOUNT) }
        require(request.amount <= 1_000_000) { throw CustomException(CustomExceptionType.INVALID_BALANCE) }

        return ResponseEntity.ok(ChargeResponse(
            balance = request.amount,
            updatedAt = LocalDateTime.now()
        ))
    }

    @GetMapping("/users/{userId}/points")
    fun getPoints(@PathVariable userId: Long): ResponseEntity<GetPointsResponse> {
        return ResponseEntity.ok(GetPointsResponse(
            balance = 0,
            updatedAt = LocalDateTime.now()
        ))
    }
}