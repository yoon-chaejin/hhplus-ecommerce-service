package kr.hhplus.be.server.application

import kr.hhplus.be.server.domain.order.model.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DataPlatform {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DataPlatform::class.java)

        fun send(order: Order) {
            logger.info("주문번호 ${order.id} : 데이터 수신")
            logger.info("구매자id : ${order.orderedBy}")
            Thread.sleep(10000)
            logger.info("주문번호 ${order.id} : 데이터 처리 완료")
            logger.info("결제금액 : ${order.paymentPrice}")
        }
    }
}