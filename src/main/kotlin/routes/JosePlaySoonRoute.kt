package com.example.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import java.sql.Connection
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor server.routing.delete
import io.ktor.server.routing.put

// GET ANIMAL SOUNDS AND SEND IMAGE OF THEM




@Serializable
fun Route.josePlaySoon(connection: Connection) {

    route("/jose-play-soon") {
        get {

        }

        post {

        }

        put {

        }

        delete {

        }


    }
}
