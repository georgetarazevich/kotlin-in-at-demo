package googlesearch.pages

import utils.selenide.collectionByXpath


object GoogleSearchPage : GooglePage(urlPath = "/", title = "Начальная страница поиска Google") {

    val search by lazy { elementByName["q"] }

    val options by lazy { elementByXpath["//ul[@role='listbox']"] }

    val optionsItems get() = options.collectionByXpath[".//li[@role='presentation']"]

    val btnK by lazy { elementByName["btnK"] }
}