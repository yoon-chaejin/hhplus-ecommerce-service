package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.coupon.CouponService
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application-test.yml"])
class CouponServiceIntegrationTests @Autowired constructor(
    val sut: CouponService
) {

    @Test
    fun `쿠폰 최대 발급 개수가 10개이고, 쿠폰 발급 요청이 20건 들어왔을 때, 10건만 성공한다`() {
        //given
        val numOfIterations = 20
        val templateId = 1L

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.issue(templateId, i.toLong())
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
    fun `쿠폰 사용 요청이 2건 이상 들어왔을 때, 1건만 성공한다`() {
        //given
        val numOfIterations = 2
        val couponId = 1L
        val userId = 1L

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.use(couponId, userId)
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
            { assertEquals(1, successCount.get()) },
        )
    }
}