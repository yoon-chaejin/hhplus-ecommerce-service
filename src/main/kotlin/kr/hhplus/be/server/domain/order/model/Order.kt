package kr.hhplus.be.server.domain.order.model

import jakarta.persistence.*
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import java.time.LocalDateTime

@Entity
@Table(name = "`order`")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @OneToOne(fetch = FetchType.LAZY)
    val coupon: IssuedCoupon? = null,
    @OneToMany(fetch = FetchType.LAZY)
    val orderProducts: List<OrderProduct>,
    val orderedBy: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    var totalPrice: Int = 0
    var paymentPrice: Int = 0

    init {
        require(orderProducts.isNotEmpty()) { "주문 상품은 필수입니다." }
        require(orderProducts.all { it.quantity > 0 }) { "주문 수량은 0보다 커야 합니다." }

        if (coupon != null) {
            require(orderedBy == coupon.ownedBy) { "쿠폰 소유자와 주문자가 일치해야 합니다." }
        }

        totalPrice = orderProducts.sumOf { it.unitPrice * it.quantity }
        paymentPrice = if (coupon != null) {
            totalPrice * coupon.template.discountRate / 100
        } else {
            totalPrice
        }
    }
}