package kr.hhplus.be.server.domain.coupon

interface IssueRequestRepository {
    fun saveRequest(key:String, value: String, score: Double)
    fun findRequests(key:String, count: Long): List<String>
}