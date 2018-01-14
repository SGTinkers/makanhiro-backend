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
import org.apache.http.HttpStatus
import routes.*
import routes.authentication.auth
import routes.authentication.jwtAuth
import java.io.File

fun main(args: Array<String>) {
  startServer()
}

fun startServer() = embeddedServer(Netty, 8080) {
    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    install(StatusPages){
        exception<Throwable> {
            val status = when (it) {
                is io.jsonwebtoken.SignatureException -> HttpStatusCode.Unauthorized
                else -> HttpStatusCode.InternalServerError
            }
            it.printStackTrace()
            call.respond(status,"Not mooning yet ☺")
        }
    }

    val path = "/api/v1"
    routing {
        intercept(ApplicationCallPipeline.Infrastructure){
            jwtAuth()
        }

        static("images") {
            staticRootFolder = File(IMAGES_DIR)
            files("images")
        }

        post(path)
        postSub(path)
        locationSub(path)
        auth(path)

    }
}.start(wait = true)



