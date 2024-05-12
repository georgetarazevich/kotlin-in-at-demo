package utils.selenide

import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By
import utils.operators.GetObject

/** Алиас Selenide.$(...) */
fun element(by: By) = Selenide.`$`(by)

/** Алиас Selenide.$$(...) */
fun SelenideElement.elements(by: By) = this.`$$`(by)

/** Алиас [SelenideElement.val] */
fun SelenideElement.value(value: String?) = this.`val`(value)

/** Получение [ElementsCollection] по xpath строке */
val SelenideElement.collectionByXpath: GetObject<String, ElementsCollection>
    get() = GetObject { xpathString -> this.elements(By.xpath(xpathString)) }