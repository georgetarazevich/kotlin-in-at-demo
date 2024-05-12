package utils.allure

import io.qameta.allure.Allure
import org.apache.logging.log4j.kotlin.logger

/**
 * Позволяет обернуть блок кода в allure-шаг внутри теста.
 *
 * ```
 * step("название шага") { <блок кода> }
 * ```
 */
fun <R> step(stepName: String, script: () -> R): R = Allure.step(stepName, script)

/**
 * Позволяет добавить текстовое представление объекта во вложение allure-отчета
 */
inline fun <reified T : Any> T.addAttachment(attachName: String = "Текстовое вложение"): T {
    logger.info("$attachName: $this")
    Allure.addAttachment(attachName, this.toString())
    return this
}