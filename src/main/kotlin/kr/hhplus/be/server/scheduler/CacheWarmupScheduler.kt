package kr.hhplus.be.server.scheduler

import kr.hhplus.be.server.application.ProductApplication
import kr.hhplus.be.server.domain.product.PopularProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
@Profile("local", "schd")
class CacheWarmupScheduler @Autowired constructor(
    private val productApplication: ProductApplication,
    private val popularProductService: PopularProductService,
) {

    @Scheduled(cron = "0 */5 * * * ?")
    fun updatePopularProducts() {
        val result = productApplication.getPopularProductsFromDatabase()
        popularProductService.setPopularProductsToCache(result)
    }
}