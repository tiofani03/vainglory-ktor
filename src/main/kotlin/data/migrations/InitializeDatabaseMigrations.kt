package com.tiooooo.data.migrations

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.initializeDatabaseMigrations() {
    val url = environment.config.property("postgres.url").getString()
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()

    val flyway = Flyway.configure()
        .dataSource(url, user, password)
        .baselineOnMigrate(true)
        .load()

    flyway.migrate()
}