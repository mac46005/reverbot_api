package com.example.routes

import com.example.data.RocketLeagueTalliesData
import com.example.responses.CreateRocketLeagueGameResultRequest
import com.example.responses.UpdateRocketLeagueGameResultRequest
import com.example.responses.toDto
import com.example.responses.toDtoList
import com.example.services.RocketLeagueTallyManagementService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.rocketLeagueTallyRoute(
    rocketLeagueTallyManagementService: RocketLeagueTallyManagementService
) {
    route("/rocket-league-tally") {
        post {
            val req = call.receive<CreateRocketLeagueGameResultRequest>()

            val id = rocketLeagueTallyManagementService.createGameResult(
                req.mode,
                req.isWin
            )

            return@post call.respond(HttpStatusCode.Created, id)
        }

        get("/todays-games") {
            val games = rocketLeagueTallyManagementService.getGameResultsFromToday()

            return@get call.respond(games.toDtoList())
        }

        get("/todays-counts") {
            val counts = rocketLeagueTallyManagementService.getGameCountsForToday()

            return@get call.respond(counts.toDto())
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val req = call.receive<UpdateRocketLeagueGameResultRequest>()

            val isUpdated = rocketLeagueTallyManagementService.updateGameResult(
                id,
                req.mode,
                req.isWin
            )

            if (isUpdated) {
                return@put call.respond(HttpStatusCode.OK)
            } else {
                return@put call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val isDeleted = rocketLeagueTallyManagementService.deleteGameResult(id)
            if (isDeleted) {
                return@delete call.respond(HttpStatusCode.NoContent)
            } else {
                return@delete call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}