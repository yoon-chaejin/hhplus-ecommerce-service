package kr.hhplus.be.server.controller.point

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.controller.point.model.*
import kr.hhplus.be.server.domain.point.PointService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "포인트 관리")
@RestController
class PointController @Autowired constructor(
    private val pointService: PointService,
) {

    @Operation(summary = "사용자 포인트 충전", description = "사용자의 포인트를 충전한다.")
    @PostMapping("/users/{userId}/points/charge")
    fun charge(@PathVariable userId: Long, @RequestBody request: ChargeRequest): ResponseEntity<ChargeResponse> {
        val response = pointService.charge(userId, request.amount).toChargeResponse()

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "사용자 포인트 조회", description = "사용자의 포인트를 조회한다.")
    @GetMapping("/users/{userId}/points")
    fun getPoints(@PathVariable userId: Long): ResponseEntity<GetPointsResponse> {
        val response = pointService.getPointByUserId(userId).toGetPointsResponse()

        return ResponseEntity.ok(response)
    }
}