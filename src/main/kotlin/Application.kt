package com.tiooooo

import com.tiooooo.config.configureContentNegotiation
import com.tiooooo.config.configureRouting
import com.tiooooo.config.configureSerialization
import com.tiooooo.data.datasource.configureDatabases
import com.tiooooo.di.configureFrameworks
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureSerialization()
    configureContentNegotiation()
    configureDatabases()
    configureRouting()
}
