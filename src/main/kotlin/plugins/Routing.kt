package com.tiooooo.plugins

import com.tiooooo.data.schemes.promo.newsRoute
import com.tiooooo.router.cityRoute
import com.tiooooo.router.rootRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        rootRoute()
        cityRoute()
        newsRoute()
    }
}
