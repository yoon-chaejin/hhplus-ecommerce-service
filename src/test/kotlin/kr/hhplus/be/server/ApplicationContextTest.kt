package kr.hhplus.be.server

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import kotlin.test.Test

@SpringBootTest
class ApplicationContextTest @Autowired constructor(
    private val applicationContext: ApplicationContext
){
    @Test
    fun contextLoads() {
        // context load test
        assertThat(applicationContext).isNotNull()

        // bean definition test
        // 1. all bean should be initialized
        // 2. duplicate bean name must not exist.
        val uniqueBeanNames: MutableSet<String> = HashSet()

        applicationContext.beanDefinitionNames.toList()
            .forEach { beanName ->
                assertThat(beanName).isNotNull()
                assertThat(uniqueBeanNames.add(beanName)).isTrue()
            }
    }
}