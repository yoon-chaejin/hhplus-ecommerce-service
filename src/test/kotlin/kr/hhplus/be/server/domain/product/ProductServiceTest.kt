package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProductServiceTest {
    private val productRepository = mock<ProductRepository>()
    private val sut: ProductService = ProductService(productRepository)

    @Test
    fun `존재하지 않는 상품id인 경우, 상품 조회 시, IllegalArgumentException이 발생한다`() {
        //given
        given(productRepository.findProductById(any())).willReturn(null)

        //when

        //then
        assertFailsWith<IllegalArgumentException> {
            sut.getProductById(0L)
        }
    }

    @Test
    fun `상품 목록 조회 시, 목록을 반환한다`() {
        //given
        val pageRequest = PageRequest.of(0, 10)
        val page = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                name = "상품명",
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
        assertInstanceOf(List::class.java, result)
    }

    @Test
    fun `조회 조건으로 Available이 주어진 경우, 상품 목록 조회 시, Available한 상품 목록을 반환한다`() {
        //given
        val status = ProductStatus.AVAILABLE
        val availableProducts = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 500,
                unitPrice = 100,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )),
        )
        val unavailableProducts = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 0,
                unitPrice = 100,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )),
        )

        given(productRepository.findProductsByRemainingQuantityGreaterThan(any(), any())).willReturn(availableProducts)
        given(productRepository.findProductsByRemainingQuantityLessThanEqual(any(), any())).willReturn(unavailableProducts)

        //when
        val result = sut.getProducts(PageRequest.of(0, 10), status)

        //then
        assertEquals(availableProducts.content, result)
    }

    @Test
    fun `조회 조건으로 Unavailable이 주어진 경우, 상품 목록 조회 시, Unavailable한 상품 목록을 반환한다`() {
        //given
        val status = ProductStatus.UNAVAILABLE
        val availableProducts = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 500,
                unitPrice = 100,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )),
        )
        val unavailableProducts = PageImpl<Product>(
            listOf(Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 0,
                unitPrice = 100,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )),
        )

        given(productRepository.findProductsByRemainingQuantityGreaterThan(any(), any())).willReturn(availableProducts)
        given(productRepository.findProductsByRemainingQuantityLessThanEqual(any(), any())).willReturn(unavailableProducts)

        //when
        val result = sut.getProducts(PageRequest.of(0, 10), status)

        //then
        assertEquals(unavailableProducts.content, result)
    }

    @Test
    fun `존재하지 않는 상품인 경우, 상품 잔여 수량 감소 시, CustomException이 발생한다`() {
        //given
        val id = 0L
        given(productRepository.findProductByIdWithLock(id)).willReturn(null)

        //when
        val result = assertFailsWith(CustomException::class) {
            sut.decreaseProductQuantity(id, 1)
        }

        //then
        assertEquals(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND, result.type)
    }
}