package com.example.routes

import java.sql.Connection
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.delete


// GET ANIMAL SOUNDS AND SEND IMAGE OF THEM




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
