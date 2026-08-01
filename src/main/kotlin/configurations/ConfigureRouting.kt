package com.example.configurations

import com.example.configuration.configureDatabase
import com.example.routes.josePlaySoon
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    val connection = configureDatabase()
    routing {
        staticResources("/static", "static")

        josePlaySoon(connection)
    }
}