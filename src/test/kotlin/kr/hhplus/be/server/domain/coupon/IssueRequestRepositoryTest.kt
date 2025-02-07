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
    fun `발급 요청 저장 후 조회 시, 조회된다`() {
        sut.saveRequest("coupon-issue-request-1", "1", System.currentTimeMillis().toDouble())
        sut.saveRequest("coupon-issue-request-1", "2", System.currentTimeMillis().toDouble())

        val result = sut.findRequests("coupon-issue-request-1", 2)

        assertEquals(2, result.size)

    }
}