package routes

import io.ktor.routing.*
import models.NotCompleted
import models.NotTested

@NotCompleted
@NotTested
fun Route.locationSub(path:String) = route("$path/locationSub") {
    get("/{userId}") { TODO() }
    post{ TODO() }
    put { TODO() }
    delete { TODO() }
}