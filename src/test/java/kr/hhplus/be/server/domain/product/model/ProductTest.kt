package kr.hhplus.be.server.domain.product.model

import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class ProductTest () {

    @Test
    fun `given 상품의 잔여 수량이 0이면 when 상품 상태 조회 시 then 상품 상태는 판매 중단으로 반환한다`() {
        //given
        val product = Product(
            id = 1L,
            remainingQuantity = 0,
            unitPrice = 100,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when
        val result = product.getStatus()

        //then
        assertEquals(ProductStatus.UNAVAILABLE, result)
    }

    @Test
    fun `given when 상품 가격이 100 단위가 아닌 상품 생성 시 then IllegalArgumentException이 발생한다`() {
        //given
        val unitPrice = 150

        //when

        //then
        assertFailsWith<IllegalArgumentException>() {
            Product(
                id = 1L,
                remainingQuantity = 0,
                unitPrice = unitPrice,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        }
    }
}