package routes

import database.PostSource
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import models.NotCompleted
import models.NotTested
import models.PostQuery
import routes.authentication.optionalLogin

@NotCompleted
@NotTested
fun Route.post(path: String) = route("$path/post"){
    get {
        val query = PostQuery.fromParams(call.parameters)
        call.respond(PostSource().getPosts(query))
    }

    post { TODO() }
    put { TODO() }
    delete { TODO() }
}



