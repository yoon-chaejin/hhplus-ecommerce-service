package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.model.OrderProductQuantitySumInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

class OrderProductServiceTest {
    private val orderProductRepository: OrderProductRepository = mock<OrderProductRepository>()
    private val sut = OrderProductService(orderProductRepository)

    @Test
    fun `인기 상품 목록 조회 시, 인기 상품 목록을 반환한다`() {
        //given
        val expected = listOf(
            OrderProductQuantitySumInfo(1L, 5L),
            OrderProductQuantitySumInfo(2L, 4L),
            OrderProductQuantitySumInfo(3L, 3L),
            OrderProductQuantitySumInfo(4L, 2L),
            OrderProductQuantitySumInfo(5L, 1L),
        )
        given(orderProductRepository.findPopularOrderProductQuantitySumInfo(any(), any())).willReturn(expected)

        //when
        val result = sut.getPopularOrderProducts(
            LocalDateTime.of(2025, 1, 1, 0, 0),
            LocalDateTime.of(2025, 1, 10, 0, 0)
        )

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `검색 시작 시점이 검색 종료 시점보다 늦은 경우, 인기 상품 목록 조회 시, IllegalArgumentException이 발생한다`() {
        //given
        val start = LocalDateTime.of(2025, 1, 10, 0, 0)
        val end = LocalDateTime.of(2025, 1, 1, 0, 0)

        //when

        //then
        assertFailsWith<IllegalArgumentException> {
            sut.getPopularOrderProducts(start, end)
        }
    }
}