package googlesearch

import googlesearch.pages.GoogleSearchPage
import googlesearch.pages.GoogleSearchResultPage

/** Страницы сервиса поиска Google */
object GoogleSearchWebApplication {

    val googleSearchPage by lazy { GoogleSearchPage }

    val googleSearchResultPage by lazy { GoogleSearchResultPage }
}