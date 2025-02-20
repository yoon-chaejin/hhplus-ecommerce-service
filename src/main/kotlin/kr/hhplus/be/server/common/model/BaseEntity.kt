package kr.hhplus.be.server.common.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @set:CreatedDate
    @set:Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.MIN

    @set:LastModifiedDate
    @set:Column
    var updatedAt: LocalDateTime = LocalDateTime.MIN

}