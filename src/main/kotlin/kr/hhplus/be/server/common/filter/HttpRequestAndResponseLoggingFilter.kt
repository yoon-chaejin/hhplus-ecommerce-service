package kr.hhplus.be.server.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class HttpRequestAndResponseLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(HttpRequestAndResponseLoggingFilter::class.java)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestToUse = ContentCachingRequestWrapper(request)
        val responseToUse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(requestToUse, responseToUse)
            val headers = requestToUse.headerNames.toList().joinToString { "$it: ${requestToUse.getHeader(it)}, " }
            log.info("HTTP Request : [${requestToUse.method} ${requestToUse.requestURI}] Headers: [$headers] Content: ${requestToUse.contentAsString}")

            log.info("HTTP Response : [${responseToUse.status} Body: ${String(responseToUse.contentAsByteArray, Charsets.UTF_8)}")
        } finally {
            responseToUse.copyBodyToResponse()
        }
    }
}