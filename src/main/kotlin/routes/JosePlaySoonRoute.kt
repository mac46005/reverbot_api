package com.example.routes

import com.example.data.CreateJosePlaySoonRequest
import com.example.data.JosePlaySoonSchema
import com.example.exceptions.PSqlExceptionHandler
import com.example.exceptions.SqlINSERTException
import com.example.repos.DefaultJosePlaySoonRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.sql.Connection
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.delete
import org.postgresql.util.PSQLException


// GET ANIMAL SOUNDS AND SEND IMAGE OF THEM




fun Route.josePlaySoon(connection: Connection) {
    val josePlaySoonRepository = DefaultJosePlaySoonRepository(JosePlaySoonSchema(connection))





    route("/jose-play-soon") {
        get {
            try {
                val list = josePlaySoonRepository.getAll()
                return@get call.respond(list)
            } catch(e: SqlINSERTException) {

            } catch (e: PSQLException) {
                val exception = PSqlExceptionHandler.handle(e, "/jose-play-soon GET")

                return@get call.respond(HttpStatusCode.InternalServerError, exception.message ?: "Uknown error")
            } catch (e: Exception) {
                return@get call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
            }
        }

        post {
            try {
                val request = call.receive<CreateJosePlaySoonRequest>()
                val id = josePlaySoonRepository.create(request.timestamp)

                return@post call.respond(id)
            } catch (e: PSQLException) {
                val exception = PSqlExceptionHandler.handle(e, "/jose-play-soon POST")
                return@post call.respond(HttpStatusCode.InternalServerError, exception.message ?: "Uknown error")
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
            }
        }

        put {
            call.respond("Not implemented yet")
        }

        delete {
            call.respond("Not implemented yet")
        }

    }
}
