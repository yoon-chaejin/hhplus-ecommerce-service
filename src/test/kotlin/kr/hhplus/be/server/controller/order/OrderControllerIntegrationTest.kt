package kr.hhplus.be.server.controller.order

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.order.model.OrderProductRequest
import kr.hhplus.be.server.controller.order.model.OrderRequest
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
class OrderControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {

    @Test
    fun `주문 결제 200`() {
        val userId = 1
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = 1L, quantity = 5)
        )

        val request = OrderRequest(
            products = products,
            couponId = 1L
        )
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
    fun `주문 결제 400 2001`() {
        val userId = 1
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = 1L, quantity = 0)
        )

        val request = OrderRequest(
            products = products,
            couponId = 1L
        )
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.INVALID_ORDER_PRODUCT_QUANTITY.resultCode))
    }

    @Test
    fun `주문 결제 400 2002`() {
        val userId = 1
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = -0L, quantity = 5)
        )

        val request = OrderRequest(
            products = products,
            couponId = 1L
        )
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND.resultCode))
    }

    @Test
    fun `주문 결제 400 2003`() {
        val userId = 1
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = 1L, quantity = 50)
        )

        val request = OrderRequest(
            products = products,
            couponId = 1L
        )
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.NOT_ENOUGH_QUANTITY.resultCode))
    }

    @Test
    fun `주문 결제 400 2101`() {
        val userId = 1
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = 1L, quantity = 5)
        )

        val request = OrderRequest(
            products = products,
            couponId = 0L
        )
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.INVALID_COUPON.resultCode))
    }

    @Test
    fun `주문 결제 400 2201`() {
        val userId = 2
        val uri = "/users/$userId/orders"
        val products = listOf(
            OrderProductRequest(id = 1L, quantity = 5)
        )

        val request = OrderRequest(
            products = products,
            couponId = 1L
        )
        val body = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(CustomExceptionType.NOT_ENOUGH_POINT.resultCode))
    }
}