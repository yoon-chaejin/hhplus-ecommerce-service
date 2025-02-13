package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.application.CouponApplication
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
@Profile("local", "scheduler")
class IssueCouponScheduler(
    private val couponApplication: CouponApplication,
) {
    @Scheduled(fixedDelay = 1000)
    fun issueCoupon() {
        couponApplication.issue()
    }
}