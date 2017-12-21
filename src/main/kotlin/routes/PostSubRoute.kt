package routes

import database.SubscriptionSource
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.ValuesMap
import models.*
import routes.authentication.requireLogin
import java.sql.SQLException

@NotCompleted
@NotTested
fun Route.postSub(path:String) = route("$path/postSub") {
    get {

    }
    post {
        val user = requireLogin()
        val postId = call.receive<ValuesMap>()["postId"]
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                if(postId == null)
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                try {
                    val res = SubscriptionSource().subToPost(user.userId, postId.toString())
                    if(!res)
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                    else
                        call.respond("Successfully subbed to $postId")
                }catch (e:SQLException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                }
            }
        }

    }
    delete {
        val user = requireLogin()
        val postId = call.receive<ValuesMap>()["postId"]
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                if(postId == null)
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request, Invalid postId", INVALID_POSTID))
                try {
                    val res = SubscriptionSource().unsubFromPost(user.userId,postId.toString())
                    when(res){
                        false -> call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                        true -> call.respond("Successfully unsubbed from $postId")
                    }

                }catch (e:SQLException){
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request, Invalid postId", INVALID_POSTID))
                }
            }
        }
    }
}