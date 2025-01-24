package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.point.model.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class PointServiceTest {
    private val pointRepository = mock(PointRepository::class.java)
    private val sut: PointService = PointService(pointRepository)

    @Test
    fun `포인트 정보가 없는 사용자인 경우, 포인트 조회 시, 잔액이 0인 포인트를 반환한다`() {
        //given
        val userId = 1L
        given(pointRepository.findPointByUserIdWithLock(userId)).willReturn(null)

        //when
        val result = sut.getPointByUserId(userId)

        //then
        assertEquals(userId, result.userId)
        assertEquals(0, result.balance)
    }

    @Test
    fun `포인트 정보가 있는 사용자인 경우, 포인트 조회 시, 해당 사용자의 포인트를 반환한다`() {
        //given
        val userId = 1L
        val balance = 500
        given(pointRepository.findPointByUserIdWithLock(userId)).willReturn(Point(userId, userId, balance))

        //when
        val result = sut.getPointByUserId(userId)

        //then
        assertEquals(userId, result.userId)
        assertEquals(balance, result.balance)
    }

    @Test
    fun `사용자와 충전 금액이 주어진 경우, 포인트 충전 시, 충전된 포인트를 반환한다`() {
        //given
        val userId = 1L
        val balance = 300
        val amount = 500
        given(pointRepository.findPointByUserIdWithLock(userId)).willReturn(Point(userId, userId, balance))
        whenever(pointRepository.save(any())).thenReturn(Point(userId, userId, balance+amount))

        //when
        val result = sut.charge(userId, amount)

        //then
        assertEquals(balance + amount, result.balance)
    }

    @Test
    fun `사용자와 충전 금액이 주어진 경우, 포인트 사용 시, 사용 후 포인트를 반환한다`() {
        //given
        val userId = 1L
        val balance = 500
        val amount = 300
        given(pointRepository.findPointByUserIdWithLock(userId)).willReturn(Point(userId, userId, balance))
        whenever(pointRepository.save(any())).thenReturn(Point(userId, userId, balance - amount))

        //when
        val result = sut.use(userId, amount)

        //then
        assertEquals(balance - amount, result.balance)
    }
}