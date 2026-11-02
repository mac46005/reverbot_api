package com.example.configurations

import com.example.data.rocketleague.RocketLeagueTalliesData
import com.example.data.createHikariDataSource
import com.example.services.RocketLeagueTallyManagementService
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.di.resolve
import java.time.Clock
import javax.sql.DataSource

fun Application.configureDependencyInjection() {
    dependencies {
        // provide { GreetingService { "Hello, World!" } }
        provide<Clock> {
            Clock.systemDefaultZone()
        }

        provide<DataSource> {
            createHikariDataSource(
                this@configureDependencyInjection.environment.config.property("postgres.url").getString(),
                this@configureDependencyInjection.environment.config.property("postgres.username").getString(),
                this@configureDependencyInjection.environment.config.property("postgres.password").getString(),
                10,
                3
            )
        }

        provide<RocketLeagueTalliesData> {
            RocketLeagueTalliesData(
                resolve<DataSource>()
            )
        }

        provide<RocketLeagueTallyManagementService>{
            RocketLeagueTallyManagementService(
                resolve<RocketLeagueTalliesData>(),
                resolve<Clock>()
            )
        }


    }
}

// fun interface GreetingService {
//      fun sayHello(): String
//}