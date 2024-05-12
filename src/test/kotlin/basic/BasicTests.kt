package basic

import com.codeborne.selenide.Condition.href
import com.codeborne.selenide.Condition.matchText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import exposed.sample.h2.db.PrepareH2TestDb
import exposed.sample.h2.db.PrepareH2TestDb.testH2Db
import exposed.sample.h2.db.model.UsersTable
import exposed.sample.h2.db.model.UsersTable.name
import github.be.dto.GitHubTagDto
import io.qameta.allure.Allure.attachment
import io.qameta.allure.Allure.step
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.lang.Integer.sum
import org.apache.logging.log4j.kotlin.logger
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasEntry
import org.hamcrest.Matchers.hasItems
import org.hamcrest.Matchers.iterableWithSize
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By

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

    @Test
    @DisplayName("Использование RESTAssured + Kotlin Extension Module + jackson-module-kotlin")
    fun restAssuredWithJacksonDataBindTest() {
        /* Kotlin Extension Module */
        val tagsJsonString = Given {
            port(443)
        } When {
            get("/repos/rest-assured/rest-assured/tags")
        } Then {
            log().all()
            statusCode(200)
            body("$", hasItem(hasEntry("name", "rest-assured-5.4.0")))
        } Extract {
            asString()
        }
        /* jackson-module-kotlin */
        val mapper = ObjectMapper().registerKotlinModule()
        val typeRef = mapper.typeFactory.constructType(object : TypeReference<List<GitHubTagDto>>() {})
        val tagList = mapper.readValue<List<GitHubTagDto>>(tagsJsonString, typeRef)
        assertThat("Список тэгов REST Assured на GitHub", tagList.map { it.name }, hasItem("rest-assured-5.4.0"))
    }

    @Test
    fun exposedH2DemoTest() {
        /* Given */
        PrepareH2TestDb.createH2TestDb()
        transaction(testH2Db) {
            /* When */
            val userNames = UsersTable.selectAll().map { it[name] }
            /* Then */
            assertThat("Имена пользователей", userNames, iterableWithSize(2))
            assertThat("Имена пользователей", userNames, hasItems(*listOf("Andrey", "Sergey").toTypedArray()))
        }
    }

    @Test
    @DisplayName("Использование selenide")
    fun selenideExampleTest() {
        /* Given */
        open("https://www.google.com/")
        /* When */
        `$`(By.name("q")).`val`("Selenide")
        /* Then */
        val options = `$`(By.xpath("//ul[@role='listbox']"))
        options.shouldBe(visible)
        options.`$$`(By.xpath(".//li[@role='presentation']"))
            .forEach { it.should(matchText("selenide")) }
        /* When */
        `$`(By.name("btnK")).click()
        /* Then */
        `$`(By.xpath("//a[@jsname and .//h3]"))
            .shouldHave(href("https://ru.selenide.org"))
            .shouldHave(text("Selenide: лаконичные и стабильные UI тесты на Java"))
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            RestAssured.baseURI = "https://api.github.com"
        }
    }
}