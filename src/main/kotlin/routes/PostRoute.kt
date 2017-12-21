package routes

import database.PostSource
import database.Validator
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.ValuesMap
import models.*
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

                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POST_STRUCT))
                }catch (e: Validator.InvalidPostObject){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",INVALID_POST_STRUCT))
                }
            }
        }
    }
    put {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val post = call.receive<ValuesMap>()
                try {
                    val res = PostSource().editPost(Validator().validatePost(post,user))
                    if(res) call.respond("Updated Success")

                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POST_STRUCT))
                }catch (e:Validator.InvalidPostObject){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POST_STRUCT))
                }
            }
        }
    }
    delete {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val post = call.receive<ValuesMap>()
                val postId = post["postId"]
                val res = PostSource().deletePost(postId,user)
                if(res) call.respond("Delete Success")
                    else call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", ERROR_DELETING_POST))
            }
        }
    }
}



