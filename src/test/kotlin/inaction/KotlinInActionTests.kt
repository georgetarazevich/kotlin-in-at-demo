package inaction

import com.codeborne.selenide.Condition.href
import com.codeborne.selenide.Condition.matchText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import github.be.dto.GitHubTagDto
import googlesearch.GoogleSearchWebApplication.googleSearchPage
import googlesearch.GoogleSearchWebApplication.googleSearchResultPage
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
import org.openqa.selenium.By
import utils.allure.addAttachment
import utils.allure.step
import utils.json.JsonSerializationUtils.constructType
import utils.json.deserializeList
import utils.json.responseAs
import utils.selenide.element
import utils.selenide.elements
import utils.selenide.value

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
        val tagList: List<GitHubTagDto> = Given {
            port(443)
        } When {
            get("/repos/rest-assured/rest-assured/tags")
        } Extract {
            responseAs(constructType(List::class.java, GitHubTagDto::class.java))
        }
        assertThat("Список тэгов REST Assured на GitHub", tagList.map { it.name }, hasItem("rest-assured-5.4.0"))
    }

    @Test
    @DisplayName("Использование selenide и Kotlin алиасов")
    fun selenideAliasesExampleTest() {
        /* Given */
        Selenide.open("https://www.google.com/")
        /* When */
        element(By.name("q")).value("Selenide")
        /* Then */
        val options = element(By.xpath("//ul[@role='listbox']"))
        options.shouldBe(visible)
        options.elements(By.xpath(".//li[@role='presentation']"))
            .forEach { it.should(matchText("selenide")) }
        /* When */
        element(By.name("btnK")).click()
        /* Then */
        element(By.xpath("//a[@jsname and .//h3]"))
            .shouldHave(href("https://ru.selenide.org"))
            .shouldHave(text("Selenide: лаконичные и стабильные UI тесты на Java"))
    }

    @Test
    @DisplayName("Использование selenide и Kotlin PageObject")
    fun selenidePageObjectExampleTest() {
        with(googleSearchPage) {
            /* Given */
            open()
            /* When */
            search.value("Selenide")
            /* Then */
            options.shouldBe(visible)
            optionsItems.forEach { it.should(matchText("selenide")) }
            /* When */
            btnK.click()
        }
        /* Then */
        googleSearchResultPage.firstSearchResult
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