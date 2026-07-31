package com.example.configuration

import io.ktor.server.application.Application
// import io.ktor.server.application.log
import java.sql.Connection
import java.sqlDriverManager

fun Application.configureDatabase(): Connection {
    Class.forName("org.postgresql.Driver")
    val url = environment.config.property("postgres.url").getString()
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()

    return DriverManager.getConnection(url, user, password)
}

