package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.product.ProductService
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application-test.yml"])
class ProductServiceIntegrationTests @Autowired constructor(
    val sut: ProductService
) {

    @Test
    fun `상품 재고 요청이 30건 들어왔을 때, 요청 내용만큼 재고가 차감된다`() {
        //given
        val numOfIterations = 5

        val productBeforeDecrease = sut.getProducts(PageRequest.of(0, 10)).filter{ it.remainingQuantity >= 5 }.first()
        assertNotNull(productBeforeDecrease)
        val quantity = 1

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.decreaseProductQuantity(productBeforeDecrease.id, quantity)
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

        val productAfterDecrease = sut.getProducts(PageRequest.of(0, 10)).filter{ it.id == productBeforeDecrease.id }.first()

        //then
        assertAll(
            { assertEquals(productBeforeDecrease.remainingQuantity - numOfIterations * quantity, productAfterDecrease.remainingQuantity) },
        )
    }
}