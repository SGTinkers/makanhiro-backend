package routes

import io.ktor.routing.*

fun Route.locationSub(path:String) = route("$path/locationSub") {
    get("/{userId}") { TODO() }
    post{ TODO() }
    put { TODO() }
    delete { TODO() }
}