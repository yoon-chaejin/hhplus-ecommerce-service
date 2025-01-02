package kr.hhplus.be.server.controller.coupon

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.coupon.model.CouponResponse
import kr.hhplus.be.server.controller.coupon.model.GetCouponsResponse
import kr.hhplus.be.server.controller.coupon.model.IssueResponse
import kr.hhplus.be.server.controller.coupon.model.IssueRequest
import kr.hhplus.be.server.domain.coupon.model.CouponStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class CouponController {

    @PostMapping("/coupon-templates/{couponTemplateId}/issue")
    fun issue(@PathVariable couponTemplateId: Long, @RequestBody request: IssueRequest): ResponseEntity<IssueResponse> {
        require(couponTemplateId > 0L) { throw CustomException(CustomExceptionType.COUPON_TEMPLATE_NOT_FOUND) }

        if (request.userId == 2L) {
            throw CustomException(CustomExceptionType.COUPON_ISSUE_FAILED)
        }

        return ResponseEntity.ok(IssueResponse(
            id = 1L,
            discountRate = 10,
            expiresAt = LocalDateTime.now().plusDays(365),
            createdAt = LocalDateTime.now(),
        ))
    }

    @GetMapping("/users/{userId}/coupons")
    fun getCoupons(@PathVariable userId: Long): ResponseEntity<GetCouponsResponse> {
        val coupons = ArrayList<CouponResponse>()

        coupons.add(CouponResponse(
            id = 1L,
            discountRate = 10,
            status = CouponStatus.USABLE,
            usedAt = null,
            expiresAt = LocalDateTime.of(2026, 1, 1, 9, 0, 0),
            createdAt = LocalDateTime.of(2025, 1, 1, 9, 0, 0),
        ))

        coupons.add(CouponResponse(
            id = 2L,
            discountRate = 10,
            status = CouponStatus.USED,
            usedAt = LocalDateTime.of(2025, 1, 1, 12, 0, 0),
            expiresAt = LocalDateTime.of(2026, 1, 1, 9, 0, 0),
            createdAt = LocalDateTime.of(2025, 1, 1, 9, 0, 0),
        ))

        coupons.add(CouponResponse(
            id = 3L,
            discountRate = 10,
            status = CouponStatus.EXPIRED,
            usedAt = null,
            expiresAt = LocalDateTime.of(2024, 12, 31, 9, 0, 0),
            createdAt = LocalDateTime.of(2023, 12, 31, 9, 0, 0),
        ))

        return ResponseEntity.ok(GetCouponsResponse(coupons))
    }
}