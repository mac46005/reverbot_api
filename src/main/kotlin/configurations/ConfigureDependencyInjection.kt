package com.example.configurations

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.configureDependencyInjection() {
    dependencies {
        // provide { GreetingService { "Hello, World!" } }
    }
}

// fun interface GreetingService {
//      fun sayHello(): String
//}