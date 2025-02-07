package kr.hhplus.be.server.domain.coupon

interface IssueHistoryRepository {
    fun findIssueHistoryByKey(key: String, value: String): Boolean
    fun saveIssueHistory(key:String, value: String)
}