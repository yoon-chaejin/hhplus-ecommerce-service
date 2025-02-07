package kr.hhplus.be.server.controller.coupon

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.controller.coupon.model.*
import kr.hhplus.be.server.domain.coupon.CouponService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "쿠폰 관리")
@RestController
class CouponController @Autowired constructor(
    val couponService: CouponService
) {

    @Operation(summary = "쿠폰 발급", description = "쿠폰 템플릿을 기반으로 선착순으로 쿠폰 발급을 요청한다.")
    @PostMapping("/coupon-templates/{couponTemplateId}/issue")
    fun issue(@PathVariable couponTemplateId: Long, @RequestBody request: IssueRequest): ResponseEntity<IssueResponse> {
        couponService.requestCouponIssue(couponTemplateId, request.userId)

        return ResponseEntity.ok().build()
    }

    @Operation(summary = "사용자 쿠폰 목록 조회", description = "사용자에게 발급된 쿠폰 목록을 조회한다.")
    @GetMapping("/users/{userId}/coupons")
    fun getCoupons(@PathVariable userId: Long): ResponseEntity<GetCouponsResponse> {
        val coupons = couponService.getIssuedCouponsByUserId(userId).map { it.toCouponResponse() }

        return ResponseEntity.ok(GetCouponsResponse(coupons = coupons))
    }
}