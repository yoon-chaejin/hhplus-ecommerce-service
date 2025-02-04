package kr.hhplus.be.server.domain.coupon.model

import jakarta.persistence.*
import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.common.model.BaseEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDateTime

@Entity
@Table(name = "issued_coupon")
class IssuedCoupon(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val template: CouponTemplate,
    val ownedBy: Long,
    val expiresAt: LocalDateTime,
    var usedAt: LocalDateTime?,
    @Version
    @ColumnDefault("0")
    val version: Long = 0L,
) : BaseEntity() {
    fun getStatus(at: LocalDateTime = LocalDateTime.now()): CouponStatus {
        if (usedAt != null) {
            return CouponStatus.USED
        }
        if (at > expiresAt) {
            return CouponStatus.EXPIRED
        }
        return CouponStatus.USABLE
    }
    fun use(userId: Long, at: LocalDateTime): IssuedCoupon {
        require(userId == ownedBy) { throw CustomException(CustomExceptionType.INVALID_COUPON) }
        require(getStatus(at) == CouponStatus.USABLE) { throw CustomException(CustomExceptionType.INVALID_COUPON) }

        usedAt = at
        return this
    }
}