package inaction

import github.be.dto.GitHubTagDto
import io.qameta.allure.Allure.attachment
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import org.apache.logging.log4j.kotlin.logger
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import utils.allure.addAttachment
import utils.allure.step
import utils.json.JsonSerializationUtils.constructType
import utils.json.deserializeList
import utils.json.responseAs

@DisplayName("Набор тестов \"Котлин в деле\"")
class KotlinInActionTests {

    @Test
    @DisplayName("Использование allure 2 с Kotlin-шагами")
    fun `test Allure with Kotlin lambda steps`() {
        step("Шаг 1") {
            step("Шаг 1.1") {}
            step("Шаг 1.2") {
                attachment("Текстовое вложение", "Текст вложения")
            }
        }
        step("Шаг 2") {
            logger.info("Лог шага 2")
        }
    }

    @Test
    @DisplayName("Использование allure 2 с вложениями на Kotlin")
    fun `test Allure with Kotlin attachments`() {
        step("Шаг 1") {
            step("Шаг 1.1") { "Текст вложения на шаге 1.1".addAttachment() }
            step("Шаг 1.2") {
                "Текст вложения на шаге 1.2".addAttachment(attachName = "Тоже текстовое вложение")
            }
        }
        step("Шаг 2") {
            logger.info("Лог шага 2")
        }
    }

    @Test
    @DisplayName("Использование RESTAssured + Kotlin JsonSerializationUtils.deserializeList")
    fun restAssuredWithJsonSerializationUtilsDeserializeListTest() {
        val tagList = Given {
            port(443)
        } When {
            get("/repos/rest-assured/rest-assured/tags")
        } Extract {
            asString().deserializeList<GitHubTagDto>()
        }
        assertThat("Список тэгов REST Assured на GitHub", tagList.map { it.name }, hasItem("rest-assured-5.4.0"))
    }

    @Test
    @DisplayName("Использование RESTAssured + Kotlin responseAs")
    fun restAssuredWithKotlinResponseAsTest() {
        val tagList = Given {
            port(443)
        } When {
            get("/repos/rest-assured/rest-assured/tags")
        } Extract {
            responseAs<List<GitHubTagDto>>(constructType(List::class.java, GitHubTagDto::class.java))
        }
        assertThat("Список тэгов REST Assured на GitHub", tagList.map { it.name }, hasItem("rest-assured-5.4.0"))
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            RestAssured.baseURI = "https://api.github.com"
        }
    }
}