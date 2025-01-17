package kr.hhplus.be.server.controller.point

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.point.model.ChargeRequest
import kr.hhplus.be.server.point.model.Point
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
class PointControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @Test
    fun `포인트 조회 200`() {
        val userId = 1
        val uri = "/users/${userId}/points"

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포인트 충전 200`() {
        val userId = 1
        val uri = "/users/${userId}/points/charge"
        val amount = 50
        val request = ChargeRequest(amount)
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
    fun `포인트 충전 400 오류 1001`() {
        val userId = 1
        val uri = "/users/${userId}/points/charge"
        val amount = 0
        val request = ChargeRequest(amount)
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.INVALID_CHARGE_AMOUNT.resultCode))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }

    @Test
    fun `포인트 충전 400 오류 1002`() {
        val userId = 2
        val uri = "/users/${userId}/points/charge"
        val amount = 1
        val request = ChargeRequest(amount)
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.INVALID_BALANCE.resultCode))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

}