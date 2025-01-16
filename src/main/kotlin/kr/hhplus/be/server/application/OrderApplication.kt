package kr.hhplus.be.server.application

import kr.hhplus.be.server.controller.order.model.OrderRequest
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.model.IssuedCoupon
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.model.Order
import kr.hhplus.be.server.domain.order.model.OrderProduct
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderApplication @Autowired constructor(
    val orderService: OrderService,
    val productService: ProductService,
    val couponService: CouponService,
    val pointService: PointService,
    ) {

    @Transactional
    fun order(userId: Long, request: OrderRequest): Order {
        val orderProducts = ArrayList<OrderProduct>()

        for (item in request.products) {
            val product = productService.decreaseProductQuantity(item.id, item.quantity)
            orderProducts.add(OrderProduct(
                productId = product.id,
                unitPrice = product.unitPrice,
                quantity = item.quantity,
            ))
        }
        var coupon: IssuedCoupon?= null

        if (request.couponId != null) {
            coupon = couponService.use(request.couponId, userId)
        }

        val order = orderService.create(
            userId = userId,
            products = orderProducts,
            coupon = coupon
        )

        pointService.use(userId, order.paymentPrice)

        DataPlatform.send(order)

        return order
    }
}