package com.tiooooo

import com.tiooooo.data.migrations.initializeDatabaseMigrations
import com.tiooooo.data.model.hero.initializeDatabaseModels
import com.tiooooo.di.configureFrameworks
import com.tiooooo.plugins.configureContentNegotiation
import com.tiooooo.plugins.configureRouting
import com.tiooooo.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    initializeDatabaseModels()
    initializeDatabaseMigrations()
    configureSerialization()
    configureContentNegotiation()
    configureRouting()
}
