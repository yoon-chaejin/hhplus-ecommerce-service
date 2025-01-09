package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.point.model.Point
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    val pointRepository: PointRepository
) {
    @Transactional
    fun getPointByUserId(userId: Long): Point {
        return pointRepository.findPointByUserIdWithLock(userId) ?: Point(userId, userId)
    }

    @Transactional
    fun charge(userId: Long, amount: Int): Point {
        val point = getPointByUserId(userId)
        point.plus(amount)
        return pointRepository.save(point)
    }

    @Transactional
    fun use(userId: Long, amount: Int): Point {
        val point = getPointByUserId(userId)
        point.minus(amount)
        return pointRepository.save(point)
    }
}