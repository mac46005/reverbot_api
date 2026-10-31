package com.example.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun createHikariDataSource(
    url: String,
    un: String,
    p: String,
    max: Int = 10,
    min: Int = 2
): HikariDataSource {
    val config = HikariConfig().apply {
        jdbcUrl = url
        username = un
        password = p
        maximumPoolSize = max
        minimumIdle = max
    }

    return HikariDataSource(config)
}