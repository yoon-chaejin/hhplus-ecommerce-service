package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.infrastructure.product.ProductRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(ProductRepositoryImpl::class)
class ProductRepositoryTest @Autowired constructor(
    val sut : ProductRepository
) {
    @Test
    fun `given when 잔여수량이 0보다 큰 상품 조회 시 then 잔여수량이 0보다 큰 상품 목록을 반환한다`() {
        //given

        //when
        val result = sut.findProductsByRemainingQuantityGreaterThan(0, PageRequest.of(0, 10))

        //then
        assertFalse { result.isEmpty() }
        result.forEach {
            assertTrue { it.remainingQuantity > 0 }
        }
    }

    @Test
    fun `given when 잔여수량이 0이하인 상품 조회 시 then 잔여수량이 0이하인 상품 목록을 반환한다`() {
        //given

        //when
        val result = sut.findProductsByRemainingQuantityLessThanEqual(0, PageRequest.of(0, 10))

        //then
        assertFalse { result.isEmpty() }
        result.forEach {
            assertTrue { it.remainingQuantity <= 0 }
        }
    }
}