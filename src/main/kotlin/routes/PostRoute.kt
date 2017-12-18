package routes

import database.PostSource
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import models.NotCompleted
import models.NotTested
import models.PostQuery
import routes.authentication.NotLoggedIn
import routes.authentication.optionalLogin
import routes.authentication.requireLogin

@NotCompleted
@NotTested
fun Route.post(path: String) = route("$path/post"){
    get {
        val query = PostQuery.fromParams(call.parameters)
        call.respond(PostSource().getPosts(query))
    }

    post {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorised")
            else -> call.respond(user)
        }
    }
    put { TODO() }
    delete { TODO() }
}



