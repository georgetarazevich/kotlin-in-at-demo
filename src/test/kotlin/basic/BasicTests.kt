package basic

import java.lang.Integer.sum
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Набор базовых тестов")
class BasicTests {

    @Test
    fun `test sum`() {
        val a = 1
        val b = 41
        assertEquals(42, sum(a, b), "Неверный результат sum($a, $b)!")
    }

    @Test
    fun `test KotlinLogging`() {
        /* Получение дополнительных данных */
        val createLogMessage: () -> String = {
            println("Получение данных для лога...")
            "Вычисляемое сообщение!"
        }
        /* Логирование */
        logger.trace("Сообщение в режиме трассировки! ${createLogMessage()}")
        logger.trace { "Еще одно сообщение в режиме трассировки! ${createLogMessage()}" }
        logger.info { "Информационное сообщение!" }
    }
}