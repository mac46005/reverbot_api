package com.example.configurations

import com.example.configuration.configureDatabase
import com.example.routes.josePlaySoon
import com.example.routes.rocketLeagueTallyRoute
import com.example.services.RocketLeagueTallyManagementService
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.routing

suspend fun Application.configureRouting() {
//    val connection = configureDatabase()
    val rocketLeagueTallyManagementService = dependencies.resolve<RocketLeagueTallyManagementService>()
    routing {
        staticResources("/static", "static")

//        josePlaySoon(connection)
        rocketLeagueTallyRoute(rocketLeagueTallyManagementService)

    }
}