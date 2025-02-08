package com.tiooooo.router

import com.tiooooo.data.schemes.city.City
import com.tiooooo.data.schemes.city.CityService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.ktor.ext.get


fun Route.cityRoute() {
    val cityService: CityService = application.get()

    post("/cities") {
        val city = call.receive<City>()

        val id = cityService.create(city)
        call.respond(HttpStatusCode.Created, id)
    }

    get("/cities/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        try {
            val city = cityService.read(id)
            call.respond(HttpStatusCode.OK, city)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    put("/cities/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        val user = call.receive<City>()
        cityService.update(id, user)
        call.respond(HttpStatusCode.OK)
    }

    delete("/cities/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        cityService.delete(id)
        call.respond(HttpStatusCode.OK)
    }

    get("/cities/hello") {
        call.respondText("Hello Worldw!")
    }
}