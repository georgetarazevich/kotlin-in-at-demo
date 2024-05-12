package googlesearch.pages

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By
import utils.allure.step
import utils.operators.GetObject
import utils.selenide.element

/** Базовая страница сервиса поиска Google */
abstract class GooglePage(val urlPath: String = "/", val title: String = "Страница сервиса поиска Google") {

    /** Быстрое получение [SelenideElement] по xpath-строке */
    val elementByXpath: GetObject<String, SelenideElement> by lazy { GetObject { xpathString -> element(By.xpath(xpathString)) } }

    /** Быстрое получение [SelenideElement] аттрибуту name */
    val elementByName: GetObject<String, SelenideElement> by lazy { GetObject { nameValue -> element(By.name(nameValue)) } }

    fun open() = step("Открываем страницу \"$title\"") {
        Selenide.open(baseUrl + urlPath)
    }

    companion object {
        const val baseUrl = "https://www.google.com"
    }
}