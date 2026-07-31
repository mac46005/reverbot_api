package com.example.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import java.sql.Connection

// GET ANIMAL SOUNDS AND SEND IMAGE OF THEM




@Serializable
fun Route.josePlaySoon(connection: Connection) {

    route("/jose-play-soon") {

    }
}