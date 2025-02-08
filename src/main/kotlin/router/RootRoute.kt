package com.tiooooo.router

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.rootRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}
