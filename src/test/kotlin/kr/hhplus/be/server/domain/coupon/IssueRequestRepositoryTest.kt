package kr.hhplus.be.server.domain.coupon

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class IssueRequestRepositoryTest @Autowired constructor(
    private val sut: IssueRequestRepository
){
    @Test
    fun `발급 요청 저장 후 조회 시, 한 번만 조회된다`() {
        sut.saveRequest("coupon-issue-request-1", "1", System.currentTimeMillis().toDouble())
        sut.saveRequest("coupon-issue-request-1", "2", System.currentTimeMillis().toDouble())

        val resultOnce = sut.findRequests("coupon-issue-request-1", 2)
        val resultTwice = sut.findRequests("coupon-issue-request-1", 2)

        assertEquals(2, resultOnce.size)
        assertEquals(0, resultTwice.size)
    }
}