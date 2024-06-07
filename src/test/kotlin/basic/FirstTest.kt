package basic

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import exposed.sample.h2.db.PrepareH2TestDb
import exposed.sample.h2.db.model.UsersTable
import github.be.dto.GitHubTagDto
import io.qameta.allure.Allure
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.logging.log4j.kotlin.logger
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
//import org.junit.jupiter.api.Test
import org.testng.annotations.Test
import org.openqa.selenium.By
import utils.PropertiesManager
import kotlin.test.assertTrue

class FirstTest : BaseTest() {

    @Test
    fun `test sum`() {

     val   baseUrl = PropertiesManager().getProperties()?.get("baseUrl")
        if (baseUrl != null) {
            Assertions.assertThat(baseUrl == "https://verapdf.duallab.com/")
        }

        val a = 1
        val b = 41
         assertThat(42 == (Integer.sum(a, b)))
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
//    @DisplayName("Использование allure 2")
    fun `test Allure with lambda steps`() {
        Allure.step("Шаг 1") { _ ->
            Allure.step("Шаг 1.1")
            Allure.step("Шаг 1.2") { _ ->
                Allure.attachment("Текстовое вложение", "Текст вложения")
            }
        }
        Allure.step("Шаг 2") { _ ->
            logger.info("Лог шага 2")
        }
    }

    @Test
//    @DisplayName("Использование RESTAssured + Kotlin Extension Module + jackson-module-kotlin")
    fun restAssuredWithJacksonDataBindTest() {
        /* Kotlin Extension Module */
        val tagsJsonString = Given {
            port(443)
        } When {
            get("/repos/rest-assured/rest-assured/tags")
        } Then {
            log().all()
            statusCode(200)
            body("$", CoreMatchers.hasItem(Matchers.hasEntry("name", "rest-assured-5.4.0")))
        } Extract {
            asString()
        }
        /* jackson-module-kotlin */
        val mapper = ObjectMapper().registerKotlinModule()
        val typeRef = mapper.typeFactory.constructType(object : TypeReference<List<GitHubTagDto>>() {})
        val tagList = mapper.readValue<List<GitHubTagDto>>(tagsJsonString, typeRef)
        MatcherAssert.assertThat(
            "Список тэгов REST Assured на GitHub",
            tagList.map { it.name },
            CoreMatchers.hasItem("rest-assured-5.4.0")
        )
    }

    @Test
    fun exposedH2DemoTest() {
        /* Given */
        PrepareH2TestDb.createH2TestDb()
        transaction(PrepareH2TestDb.testH2Db) {
            /* When */
            val userNames = UsersTable.selectAll().map { it[UsersTable.name] }
            /* Then */
            MatcherAssert.assertThat("Имена пользователей", userNames, Matchers.iterableWithSize(2))
            MatcherAssert.assertThat(
                "Имена пользователей",
                userNames,
                Matchers.hasItems(*listOf("Andrey", "Sergey").toTypedArray())
            )
        }
    }

    @Test
//    @DisplayName("Использование selenide")
    fun selenideExampleTest() {
        /* Given */
        Selenide.open("https://www.google.com/")
        /* When */
        Selenide.`$`(By.name("q")).`val`("Selenide")
        /* Then */
        val options = Selenide.`$`(By.xpath("//ul[@role='listbox']"))
        options.shouldBe(Condition.visible)
        options.`$$`(By.xpath(".//li[@role='presentation']"))
            .forEach { it.should(Condition.matchText("selenide")) }
        /* When */
        Selenide.`$`(By.name("btnK")).click()
        /* Then */
        Selenide.`$`(By.xpath("//a[@jsname and .//h3]"))
            .shouldHave(Condition.href("https://ru.selenide.org"))
            .shouldHave(Condition.text("Selenide: лаконичные и стабильные UI тесты на Java"))
    }

//    fun q1 (){
//        logger.info { "Moving ..." }
//    }



//    companion object {
//        @JvmStatic
//        @BeforeAll
//        fun beforeAll() {
//            RestAssured.baseURI = "https://api.github.com"
//        }
//    }
}
