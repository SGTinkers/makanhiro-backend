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
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val postId = call.receive<ValuesMap>()["postId"]
                try {
                    val res = SubscriptionSource().subToPost(user.userId, postId.toString())
                    if(!res)
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                    else
                        call.respond("Successfully subbed")
                }catch (e:SQLException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request", INVALID_POSTID))
                }
            }
        }

    }
    delete{ TODO() }
}