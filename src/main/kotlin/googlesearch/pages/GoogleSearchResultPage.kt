package googlesearch.pages

object GoogleSearchResultPage : GooglePage(urlPath = "/search?q=", title = "Страница поиска Google с результатами") {

    val firstSearchResult by lazy { elementByXpath["//a[@jsname and .//h3]"] }
}