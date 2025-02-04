package kr.hhplus.be.server.domain.order.model

import jakarta.persistence.*
import kr.hhplus.be.server.common.model.BaseEntity

@Entity
@Table(name = "order_product")
class OrderProduct(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val quantity: Int,
    val unitPrice: Int,
    val productId: Long,
) : BaseEntity()