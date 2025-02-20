package kr.hhplus.be.server

import jakarta.annotation.PreDestroy
import org.apache.kafka.streams.Topology.AutoOffsetReset
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.kafka.ConfluentKafkaContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@Profile("testcontainers")
class TestcontainersConfiguration {
    @PreDestroy
    fun preDestroy() {
        if (mySqlContainer.isRunning) mySqlContainer.stop()
        if (redisContainer.isRunning) redisContainer.stop()
        if (kafkaContainer.isRunning) kafkaContainer.stop()
    }

    companion object {
        val mySqlContainer: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("hhplus")
            .withUsername("test")
            .withPassword("test")
            .apply { start() }

        // Redis 컨테이너 설정 (GenericContainer 사용)
        val redisContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:8.0-M03-alpine"))
            .withExposedPorts(6379)
            .apply { start() }

        // Kafka 컨테이너 설정
        val kafkaContainer = ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.8.0"))
            .apply { start() }


        init {
            // MySQL 관련 시스템 프로퍼티 설정
            System.setProperty("spring.datasource.url", mySqlContainer.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC")
            System.setProperty("spring.datasource.username", mySqlContainer.username)
            System.setProperty("spring.datasource.password", mySqlContainer.password)

            // Redis 관련 시스템 프로퍼티 설정
            System.setProperty("spring.data.redis.host", redisContainer.host)
            System.setProperty("spring.data.redis.port", redisContainer.firstMappedPort.toString())

            // Kafka 관련 시스템 프로퍼티 설정
            System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.bootstrapServers)
            System.setProperty("spring.kafka.consumer.auto-offset-reset", "earliest")
        }
    }
}
