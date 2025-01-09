package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.product.model.Product
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProductServiceTest {
    private val productRepository = mock<ProductRepository>()
    private val sut: ProductService = ProductService(productRepository)

    @Test
    fun `given when 상품 목록 조회 시 then 목록을 반환한다`() {
        //given
        val pageRequest = PageRequest.of(0, 10)
        val page = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                remainingQuantity = 500,
                unitPrice = 100,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )),
        )

        given(productRepository.findProducts(page = pageRequest)).willReturn(page)

        //when
        val result = sut.getProducts(pageRequest)

        //then
        assertInstanceOf(Page::class.java, result)
    }

    @Test
    fun `given when 인기 상품 목록 조회 시 then 인기 상품 목록을 반환한다`() {
        //given
        given(productRepository.findPopularProducts()).willReturn(listOf(
            Product(
                id = 1L,
                remainingQuantity = 500,
                unitPrice = 100,
            ),
            Product(
                id = 2L,
                remainingQuantity = 500,
                unitPrice = 100,
            ),
            Product(
                id = 3L,
                remainingQuantity = 500,
                unitPrice = 100,
            ),
            Product(
                id = 4L,
                remainingQuantity = 500,
                unitPrice = 100,
            ),
            Product(
                id = 5L,
                remainingQuantity = 500,
                unitPrice = 100,
            )
        ))

        //when
        val result = sut.getPopularProducts()

        //then
        assertInstanceOf(List::class.java, result)
        assertEquals(5, result.size)
    }

    @Test
    fun `given 존재하지 않는 상품에 대해 when 상품 잔여 수량 감소 시 then CustomException 이 발생한다`() {
        //given
        val id = 0L
        given(productRepository.findProductById(id)).willReturn(null)

        //when
        val result = assertFailsWith(CustomException::class) {
            sut.decreaseProductQuantity(id, 1)
        }

        //then
        assertEquals(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND, result.type)
    }
}