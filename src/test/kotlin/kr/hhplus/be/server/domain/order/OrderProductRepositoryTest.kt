package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.TestcontainersConfiguration
import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import kr.hhplus.be.server.infrastructure.order.OrderProductRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class, OrderProductRepositoryImpl::class)
class OrderProductRepositoryTest @Autowired constructor(
    private val sut: OrderProductRepository,
) {
    @Test
    fun `7일 전, 3일 전, 지금 주문이 이루어진 경우, 기간 내 인기 상품 주문량 조회 시, 기간 내 판매량 상위 5건이 반환된다`() {
        //given
        val now = LocalDateTime.of(2025, 1, 10, 0, 0, 0)
        val expected = listOf(
            OrderProductQuantitySumInfo(5L , 6L),
            OrderProductQuantitySumInfo(4L , 5L),
            OrderProductQuantitySumInfo(2L , 4L),
            OrderProductQuantitySumInfo(3L , 3L),
            OrderProductQuantitySumInfo(1L , 2L),
        )

        //when
        val result = sut.findPopularOrderProductQuantitySumInfo(now.minusDays(3), now)

        //then
        assertEquals(5, result.size)
        for(i in result.indices) {
            assertEquals(expected.get(i), result.get(i))
        }
    }
}