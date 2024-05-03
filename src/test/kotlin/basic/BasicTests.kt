package basic

import io.qameta.allure.Allure.attachment
import io.qameta.allure.Allure.step
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

    @Test
    @DisplayName("Использование allure 2")
    fun `test Allure with lambda steps`() {
        step("Шаг 1") { _ ->
            step("Шаг 1.1")
            step("Шаг 1.2") { _ ->
                attachment("Текстовое вложение", "Текст вложения")
            }
        }
        step("Шаг 2") { _ ->
            logger.info("Лог шага 2")
        }
    }
}