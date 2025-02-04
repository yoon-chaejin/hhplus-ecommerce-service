package kr.hhplus.be.server

import kr.hhplus.be.server.helper.DataCleanup
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServiceIntegrationTest {
    @Autowired
    protected lateinit var databaseTestFixture: DatabaseTestFixture

    @Autowired
    protected lateinit var dataCleanup: DataCleanup

    @BeforeEach
    fun cleanup() {
        dataCleanup.execute()
    }
}