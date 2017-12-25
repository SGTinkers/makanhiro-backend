package routes

import database.PostSource
import database.Validator
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.ValuesMap
import models.*
import routes.authentication.requireLogin


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
                val multipart = call.receiveMultipart()
                try{
                    val validatedMultiPart = Validator().validateMultiPartPost(user,multipart)
                    val res = PostSource().createPost(Validator().validatePost(validatedMultiPart,user))
                    if(res)
                        call.respond("Posted Success")
                    else
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",INVALID_POST_STRUCT))
                }catch (e:InvalidPostObject){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",INVALID_POST_STRUCT))
                }catch (e:FileSizeTooBig){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",FILE_SIZE_TOO_BIG))
                }
            }
        }
    }
    put {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val multipart = call.receiveMultipart()
                try{
                    val validatedMultiPart = Validator().validateMultiPartPost(user,multipart)
                    val res = PostSource().editPost(Validator().validatePost(validatedMultiPart,user))
                    if(res)
                        call.respond("Post Update success")
                    else
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",INVALID_POST_STRUCT))
                }catch (e:InvalidPostObject){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",INVALID_POST_STRUCT))
                }catch (e:FileSizeTooBig){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request",FILE_SIZE_TOO_BIG))
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



