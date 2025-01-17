package kr.hhplus.be.server.controller.product

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
) {

    @Test
    fun `상품 목록 조회 200`() {
        val uri = "/products"
        val queryParams = LinkedMultiValueMap<String, String>()

        mockMvc.perform(MockMvcRequestBuilders.get(uri).queryParams(queryParams))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `인기 상품 목록 조회 200`() {
        val uri = "/products/popular"

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}