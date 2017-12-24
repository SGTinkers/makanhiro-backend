import database.IMAGES_DIR
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.ErrorMsg
import routes.*
import routes.authentication.auth
import routes.authentication.jwtAuth
import java.io.File

fun main(args: Array<String>) {
  startServer()
}

fun startServer() = embeddedServer(Netty, 8080) {

    install(StatusPages){
        status(HttpStatusCode.BadRequest) {
            call.response.status(HttpStatusCode.BadRequest)
            call.respond(ErrorMsg("Invalid Format found!",0))
        }
        status(HttpStatusCode.Forbidden){
            call.response.status(HttpStatusCode.Forbidden)
            call.respond(ErrorMsg("Forbidden",1))
        }
        status(HttpStatusCode.NoContent){
            call.response.status(HttpStatusCode.NoContent)
            call.respond(ErrorMsg("Resource not found",2))
        }
    }
    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    val path = "/api/v1"
    routing {
        intercept(ApplicationCallPipeline.Infrastructure){
            jwtAuth()
        }

        post(path)
        postSub(path)
        locationSub(path)
        auth(path)
        images(path)
        static("images") {
            TODO("сука Блять")
        }
    }
}.start(wait = true)



