package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.point.model.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointJpaRepository : JpaRepository<Point, Long> {
    fun findByUserId(userId: Long): Point?
}