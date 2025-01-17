package kr.hhplus.be.server.controller.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.coupon.model.IssueRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {

    @Test
    fun `쿠폰 목록 조회 200`() {
        val userId = 1
        val uri = "/users/${userId}/coupons"

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `쿠폰 발급 200`() {
        val couponTemplateId = 1
        val uri = "/coupon-templates/${couponTemplateId}/issue"
        val userId = 1L
        val request = IssueRequest(userId = userId)
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `쿠폰 발급 400 3001`() {
        val couponTemplateId = 0
        val uri = "/coupon-templates/${couponTemplateId}/issue"
        val userId = 1L
        val request = IssueRequest(userId = userId)
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.error")
                    .value(CustomExceptionType.COUPON_TEMPLATE_NOT_FOUND.resultCode)
            )
    }

    @Test
    fun `쿠폰 발급 400 3002`() {
        val couponTemplateId = 2
        val uri = "/coupon-templates/$couponTemplateId/issue"
        val userId = 1L
        val request = IssueRequest(userId = userId)
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.error")
                    .value(CustomExceptionType.COUPON_ISSUE_FAILED.resultCode)
            )
    }
}