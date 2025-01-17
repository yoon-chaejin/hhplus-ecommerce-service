package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.point.PointService
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class PointServiceIntegrationTests @Autowired constructor(
    private val sut: PointService,
) {

    @Test
    fun `포인트 충전 및 차감 요청이 각 5건씩 들어왔을 때, 최종 금액은 각 충전 및 차감 금액을 계산한 결과이다`() {
        //given
        val userId = 10L

        val chargeRequests: List<Int> = listOf(50, 300, 200, 50, 100)
        val useRequests: List<Int> = listOf(500, 50, 10, 20, 10)
        val numOfIterations = chargeRequests.size + useRequests.size

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..chargeRequests.size) {
            executorService.execute {
                try {
                    sut.charge(userId, chargeRequests[i-1])
                    successCount.getAndIncrement()
                } catch(e: Exception) {
                    failCount.getAndIncrement()
                } finally {
                    doneSignal.countDown()
                }
            }
        }

        for (i in 1..useRequests.size) {
            executorService.execute {
                try {
                    sut.use(userId, useRequests[i-1])
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

        val result = sut.getPointByUserId(userId)

        //then
        assertAll(
            { assertEquals(numOfIterations, successCount.get()) },
            { assertEquals(1000 + chargeRequests.sum() - useRequests.sum(), result.balance) },
        )
    }
}