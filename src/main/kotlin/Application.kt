import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import routes.*

fun main(args: Array<String>) {
  startServer()
}

fun startServer() = embeddedServer(Netty, 8080) {
    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    val path = "/api/v1"
    routing {
        post(path)
    }
}.start(wait = true)