package kr.hhplus.be.server.integration

import kr.hhplus.be.server.ServiceIntegrationTest
import kr.hhplus.be.server.domain.coupon.CouponService
import org.junit.jupiter.api.assertAll
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals

class CouponServiceIntegrationTests @Autowired constructor(
    val sut: CouponService,
) : ServiceIntegrationTest() {
    val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `쿠폰 최대 발급 개수가 10개이고, 쿠폰 발급 요청이 20건 들어왔을 때, 10건만 성공한다`() {
        //given
        val numOfIterations = 20
        val template = databaseTestFixture.createCouponTemplate()

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.issue(template.id, i.toLong())
                    successCount.getAndIncrement()
                } catch(e: Exception) {
                    failCount.getAndIncrement()
                } finally {
                    doneSignal.countDown()
                }
            }
        }

        doneSignal.await()
        executorService.shutdown()

        //then
        assertAll(
            { assertEquals(10, successCount.get()) },
        )
    }

    @Test
    fun `쿠폰 사용 요청이 2건이 들어왔을 때, 1건만 성공한다`() {
        //given
        val numOfIterations = 2

        val userId = 1L
        val template = databaseTestFixture.createCouponTemplate()
        val coupon = databaseTestFixture.createIssuedCoupon(ownedBy = userId, template = template)

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        val measuredTime = measureTimeMillis {
            //when
            for (i in 1..numOfIterations) {
                executorService.execute {
                    try {
                        sut.use(coupon.id, userId)
                        successCount.getAndIncrement()
                    } catch(e: Exception) {
                        failCount.getAndIncrement()
                    } finally {
                        doneSignal.countDown()
                    }
                }
            }

            doneSignal.await()
            executorService.shutdown()
        }
        logger.info("소요 시간 : $measuredTime")

        //then
        assertAll(
            { assertEquals(1, successCount.get()) },
        )
    }
}