package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.point.model.Point

interface PointRepository {
    fun findPointByUserIdWithLock(userId: Long): Point?

    fun save(point: Point): Point
}
