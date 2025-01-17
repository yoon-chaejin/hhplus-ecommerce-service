package kr.hhplus.be.server.integration

import kr.hhplus.be.server.application.OrderApplication
import kr.hhplus.be.server.controller.order.model.OrderProductRequest
import kr.hhplus.be.server.controller.order.model.OrderRequest
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class OrderApplicationIntegrationTests @Autowired constructor(
    val sut: OrderApplication,
) {
    @Test
    fun `재고가 충분한, 동일한 상품을 포함하는 주문-결제 요청이 2건 들어왔을 때, 2건 모두 성공한다`() {
        //given
        val numOfIterations = 2

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.order(i.toLong(), OrderRequest(
                        products = listOf(
                            OrderProductRequest(6L, 1)
                        ),
                        couponId = null
                    ))
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
            { assertEquals(2, successCount.get()) },
        )
    }
}