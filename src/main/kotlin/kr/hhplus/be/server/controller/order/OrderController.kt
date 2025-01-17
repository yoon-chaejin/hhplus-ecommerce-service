package kr.hhplus.be.server.controller.order

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.application.OrderApplication
import kr.hhplus.be.server.controller.order.model.OrderRequest
import kr.hhplus.be.server.controller.order.model.OrderResponse
import kr.hhplus.be.server.controller.order.model.toOrderResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "주문/결제")
@RestController
class OrderController(
    private val orderApplication: OrderApplication
) {

    @Operation(summary = "상품 주문&결제", description = "주문 상품 목록에 대해 주문 및 결제를 진행한다.")
    @PostMapping("/users/{userId}/orders")
    fun order(
        @PathVariable userId: Long,
        @RequestBody request: OrderRequest
    ): OrderResponse {
        return orderApplication.order(userId, request).toOrderResponse()
    }
}