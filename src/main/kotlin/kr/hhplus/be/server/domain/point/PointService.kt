package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.point.model.Point
import org.springframework.stereotype.Service

@Service
class PointService(
    val pointRepository: PointRepository
) {

    fun getPointByUserId(userId: Long): Point {
        return pointRepository.findPointByUserId(userId) ?: Point(userId, userId)
    }

    fun charge(userId: Long, amount: Int): Point {
        val point = getPointByUserId(userId)
        point.plus(amount)
        return pointRepository.save(point)
    }
}