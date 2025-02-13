package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.TestcontainersConfiguration
import kr.hhplus.be.server.domain.coupon.model.CouponTemplate
import kr.hhplus.be.server.infrastructure.coupon.CouponTemplateJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.CouponTemplateRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class, CouponTemplateRepositoryImpl::class)
class CouponTemplateRepositoryTest @Autowired constructor(
    private val sut: CouponTemplateRepository,
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) {
    @BeforeEach
    fun clearDatabase() {
        couponTemplateJpaRepository.deleteAllInBatch()
    }

    @Test
    fun `발급 가능한 쿠폰 목록 조회 시, 발급 개수가 최대 발급 개수보다 작은 모든 쿠폰이 조회된다`() {
        //given
        val couponTemplates = listOf(
            CouponTemplate(
                id = 0L,
                discountRate = 10,
                issueCount = 0,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            ),
            CouponTemplate(
                id = 0L,
                discountRate = 10,
                issueCount = 5,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            ),
            CouponTemplate(
                id = 0L,
                discountRate = 10,
                issueCount = 10,
                maxIssueCount = 10,
                issuableUntil = LocalDateTime.now().plusYears(1),
            ),
            CouponTemplate(
                id = 0L,
                discountRate = 10,
                issueCount = 15,
                maxIssueCount = 15,
                issuableUntil = LocalDateTime.now().plusYears(1),
            )
        )
        val expected = couponTemplateJpaRepository.saveAll(couponTemplates).filter { it.issueCount < it.maxIssueCount }

        //when
        val result = sut.findCouponTemplatesIssuable()

        //then
        assertEquals(expected.size, result.size)
    }
}