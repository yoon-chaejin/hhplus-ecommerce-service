package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.point.model.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl @Autowired constructor(
    private val pointJpaRepository: PointJpaRepository,
) : PointRepository {
    override fun findPointByUserId(userId: Long): Point? {
        return pointJpaRepository.findByUserId(userId)
    }

    override fun save(point: Point): Point {
        return pointJpaRepository.save(point)
    }

}