package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.OrderProductService
import kr.hhplus.be.server.domain.product.PopularProductService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.model.Product
import kr.hhplus.be.server.domain.product.model.ProductStatus
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.springframework.data.domain.PageRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductApplicationTest {
    private val productService = mock<ProductService>()
    private val orderProductService = mock<OrderProductService>()
    private val popularProductService = mock<PopularProductService>()
    private val productApplication = ProductApplication(productService, orderProductService, popularProductService)


    @Test
    fun `상태에 대한 검색 조건이 없는 경우, 상품 조회 시, 두 상태 모두 조회된다`() {
        //given
        val products = listOf(
            Product(
                id = 1L,
                unitPrice = 100,
                name = "허재님의 생일케이크",
                remainingQuantity = 10
            ),
            Product(
                id = 2L,
                unitPrice = 100,
                name = "한결님의 응원봉",
                remainingQuantity = 0
            )
        )
        val expected = products
        given(productService.getProducts(any())).willReturn(products)
        given(productService.getProducts(any(), eq(ProductStatus.AVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.AVAILABLE })
        given(productService.getProducts(any(), eq(ProductStatus.UNAVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.UNAVAILABLE })

        //when
        val result = productApplication.getProducts(PageRequest.of(0, 10), null)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `상태에 대한 검색 조건이 AVAILABLE인 경우, 상품 조회 시, 유효한 상품만 조회된다`() {
        //given
        val products = listOf(
            Product(
                id = 1L,
                unitPrice = 100,
                name = "허재님의 생일케이크",
                remainingQuantity = 10
            ),
            Product(
                id = 2L,
                unitPrice = 100,
                name = "한결님의 응원봉",
                remainingQuantity = 0
            )
        )
        val expected = products.filter { it.getStatus() == ProductStatus.AVAILABLE }
        given(productService.getProducts(any())).willReturn(products)
        given(productService.getProducts(any(), eq(ProductStatus.AVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.AVAILABLE })
        given(productService.getProducts(any(), eq(ProductStatus.UNAVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.UNAVAILABLE })

        //when
        val result = productApplication.getProducts(PageRequest.of(0, 10), ProductStatus.AVAILABLE)

        //then
        assertEquals(expected, result)
    }


    @Test
    fun `상태에 대한 검색 조건이 UNAVAILABLE인 경우, 상품 조회 시, 유효하지 않은 상품만 조회된다`() {
        //given
        val products = listOf(
            Product(
                id = 1L,
                unitPrice = 100,
                name = "허재님의 생일케이크",
                remainingQuantity = 10
            ),
            Product(
                id = 2L,
                unitPrice = 100,
                name = "한결님의 응원봉",
                remainingQuantity = 0
            )
        )
        val expected = products.filter { it.getStatus() == ProductStatus.UNAVAILABLE }
        given(productService.getProducts(any())).willReturn(products)
        given(productService.getProducts(any(), eq(ProductStatus.AVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.AVAILABLE })
        given(productService.getProducts(any(), eq(ProductStatus.UNAVAILABLE))).willReturn(products.filter { it.getStatus() == ProductStatus.UNAVAILABLE })

        //when
        val result = productApplication.getProducts(PageRequest.of(0, 10), ProductStatus.UNAVAILABLE)

        //then
        assertEquals(expected, result)
    }
}