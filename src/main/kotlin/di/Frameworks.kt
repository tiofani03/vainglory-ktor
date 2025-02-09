package com.tiooooo.di

import com.tiooooo.data.connection.connectToPostgres
import com.tiooooo.data.model.city.CityService
import com.tiooooo.data.model.hero.heroModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import java.sql.Connection
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            listOf(
                module {
                    single<Connection> { connectToPostgres(embedded = false) }
                    single { CityService(get()) }
                },
                heroModule
            )
        )
    }
}
