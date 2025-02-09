package com.tiooooo.data.schemes.promo

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("Scraper")

fun scrapeVaingloryNews(): List<NewsArticle> {
    log.info("Starting to scrape Vainglory news...")

    val articles = mutableListOf<NewsArticle>()

    try {
        val doc: Document = Jsoup.connect("https://www.vainglorygame.com/news/").get()
        log.info("Successfully fetched HTML from Vainglory news page")
        log.info("HTML content: \n${doc.body().html()}")  // Print seluruh body HTML

        val elements = doc.select("result relative container")
        log.info("Found ${elements.size} articles")

        doc.select(".js-news-carousel .slide").forEach { element ->
            val title = element.select("h1.white").text()
            val link = element.select("a.button-big").attr("href")
            val imageUrl = element.select(".bg-image").attr("style")
                .substringAfter("background-image: url(")
                .substringBefore(");")

            articles.add(NewsArticle(title, link, imageUrl))
        }

    } catch (e: Exception) {
        log.error("Error while scraping news: ${e.message}", e)
    }

    log.info("Scraping completed. Found ${articles.size} articles.")
    return articles
}

@Serializable
data class NewsArticle(val title: String, val description: String, val link: String)

fun Route.newsRoute() {
    get("/scrape-news") {
        val news = scrapeVaingloryNews()
        call.respond(HttpStatusCode.OK, news)
    }
}