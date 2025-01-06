package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.point.model.Point
import org.springframework.stereotype.Repository

@Repository
interface PointRepository {
    fun findPointByUserId(userId: Long): Point?

    fun save(point: Point): Point
}
