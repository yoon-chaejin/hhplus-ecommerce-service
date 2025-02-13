package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.coupon.CouponService
import org.springframework.stereotype.Service

@Service
class CouponApplication (
    private val couponService: CouponService,
){
    fun issue() {
        val couponTemplates = couponService.getCouponTemplatesIssuable()
        for (couponTemplate in couponTemplates) {
            val issueRequests = couponService.getCouponIssueRequests(couponTemplate.id, couponTemplate.maxIssueCount - couponTemplate.issueCount)
            for (issueRequest in issueRequests) {
                couponService.issue(couponTemplate.id, issueRequest.toLong())
            }
        }
    }
}