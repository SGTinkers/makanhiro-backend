package routes

import database.PostSource
import database.Validator
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.ValuesMap
import models.ErrorMsg
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
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val post = call.receive<ValuesMap>()
                try {
                    val res = PostSource().createPost(Validator().validatePost(post,user))
                    if(res) call.respond("Posted Success")
                        else call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",564))
                }catch (e: Validator.InvalidPostObject){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",564))
                }
            }
        }
    }
    put { TODO() }
    delete { TODO() }
}



