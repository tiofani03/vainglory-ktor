package com.tiooooo.config

import com.tiooooo.router.cityRoute
import com.tiooooo.router.rootRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        rootRoute()
        cityRoute()
    }
}
