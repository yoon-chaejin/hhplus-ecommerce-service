package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import java.time.LocalDateTime

class Order(
    val id: Long = 0,
    val coupon: IssuedCoupon? = null,
    val orderProducts: List<OrderProduct>,
    val orderedBy: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    var totalPrice: Int = 0
    var paymentPrice: Int = 0

    init {
        totalPrice = orderProducts.sumOf { it.unitPrice * it.quantity }
        paymentPrice = if (coupon != null) {
            totalPrice * coupon.template.discountRate / 100
        } else {
            totalPrice
        }
    }
}