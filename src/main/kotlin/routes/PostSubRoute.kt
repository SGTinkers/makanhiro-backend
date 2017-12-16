package routes

import io.ktor.routing.*
import models.NotCompleted
import models.NotTested

@NotCompleted
@NotTested
fun Route.postSub(path:String) = route("$path/postSub") {
    get("/{userId}") { TODO() }
    post { TODO() }
    put{ TODO() }
    delete{ TODO() }
}