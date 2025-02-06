package kr.hhplus.be.server.domain.coupon

interface IssueRequestRepository {
    fun save(key:String, value: String, score: Double)
}