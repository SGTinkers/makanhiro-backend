package routes

import io.ktor.routing.*

fun Route.postSub(path:String) = route("$path/postSub") {
    get("/{userId}") { TODO() }
    post { TODO() }
    put{ TODO() }
    delete{ TODO() }
}