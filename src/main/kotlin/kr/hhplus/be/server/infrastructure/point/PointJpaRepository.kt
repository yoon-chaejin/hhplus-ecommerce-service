package kr.hhplus.be.server.infrastructure.point

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.point.model.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface PointJpaRepository : JpaRepository<Point, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(userId: Long): Point?

    @Lock(LockModeType.OPTIMISTIC)
    fun findWithOptimisticLockByUserId(userId: Long): Point?
}