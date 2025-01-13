package kr.hhplus.be.server.controller.point

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.point.model.ChargeRequest
import kr.hhplus.be.server.controller.point.model.ChargeResponse
import kr.hhplus.be.server.controller.point.model.GetPointsResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "포인트 관리")
@RestController
class PointController {

    @Operation(summary = "사용자 포인트 충전", description = "사용자의 포인트를 충전한다.")
    @PostMapping("/users/{userId}/points/charge")
    fun charge(@PathVariable userId: Long, @RequestBody request: ChargeRequest): ResponseEntity<ChargeResponse> {
        require(request.amount > 0) { throw CustomException(CustomExceptionType.INVALID_CHARGE_AMOUNT) }
        require(request.amount <= 1_000_000) { throw CustomException(CustomExceptionType.INVALID_BALANCE) }

        return ResponseEntity.ok(
            ChargeResponse(
            balance = request.amount,
            updatedAt = LocalDateTime.now()
        )
        )
    }

    @Operation(summary = "사용자 포인트 조회", description = "사용자의 포인트를 조회한다.")
    @GetMapping("/users/{userId}/points")
    fun getPoints(@PathVariable userId: Long): ResponseEntity<GetPointsResponse> {
        return ResponseEntity.ok(GetPointsResponse(
            balance = 0,
            updatedAt = LocalDateTime.now()
        ))
    }
}