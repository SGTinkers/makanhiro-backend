package routes

import io.ktor.routing.*
import models.NotCompleted
import models.NotTested

@NotCompleted
@NotTested
fun Route.postSub(path:String) = route("$path/postSub") {
    get {

    }
    post { TODO() }
    delete{ TODO() }
}