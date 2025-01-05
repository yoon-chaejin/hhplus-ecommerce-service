package kr.hhplus.be.server.controller.order

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.controller.order.model.OrderProductResponse
import kr.hhplus.be.server.controller.order.model.OrderRequest
import kr.hhplus.be.server.controller.order.model.OrderResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class OrderController {

    @PostMapping("/users/{userId}/orders")
    fun order(
        @PathVariable userId: Long,
        @RequestBody request: OrderRequest
    ): OrderResponse {
        val products = ArrayList<OrderProductResponse>()
        var totalPrice = 0

        for (product in request.products) {
            require(product.quantity > 0) { throw CustomException(CustomExceptionType.INVALID_ORDER_PRODUCT_QUANTITY) }
        }

        for (product in request.products) {
            require(product.id > 0) { throw CustomException(CustomExceptionType.ORDER_PRODUCT_NOT_FOUND) }
            require(product.quantity < 50) { throw CustomException(CustomExceptionType.NOT_ENOUGH_QUANTITY) }

            products.add(OrderProductResponse(
                id = product.id,
                name = "상품명",
                unitPrice = 50,
                quantity = product.quantity,
            ))

            totalPrice += 50 * product.quantity
        }

        var paymentPrice = totalPrice

        if (request.couponId != null) {
            require(request.couponId > 0) { throw CustomException(CustomExceptionType.INVALID_COUPON) }

            paymentPrice = (totalPrice * 0.9).toInt()
        }

        if (userId == 2L) {
            throw CustomException(CustomExceptionType.NOT_ENOUGH_POINT)
        }

        return OrderResponse(
            orderId = 1L,
            products = products,
            totalPrice = totalPrice,
            paymentPrice = paymentPrice,
            orderedAt = LocalDateTime.now(),
        )
    }
}