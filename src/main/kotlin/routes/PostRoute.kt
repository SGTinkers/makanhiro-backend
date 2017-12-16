package routes

import database.PostSource
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*



fun Route.post(path: String) = route("$path/post"){
    get { call.respond(PostSource().getPosts()) }

    get("/{postId}") {
        val id = call.parameters["postId"]
        when(id) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> when(PostSource().getPostById(id)) {
                null -> call.respond(HttpStatusCode.NotFound)
                else -> PostSource().getPostById(id)?.let { it1 -> call.respond(it1) }
            }
        }
    }

    get("/location/{locationId}"){
        val id = try { call.parameters["locationId"]?.toInt() } catch (e:NumberFormatException) {
            null
        }
        when(id) {
            null -> call.respond(HttpStatusCode.BadRequest)
            else -> {
                val res= PostSource().getPostsByLocationId(id)
                when(res.isEmpty()) {
                    true -> call.respond(HttpStatusCode.NotFound)
                    else -> res.let { it1 -> call.respond(it1) }
                }
            }
        }
    }

    get("/user/{userId}"){
        val userId = call.parameters["userId"]
        when(userId){
            null -> call.respond(HttpStatusCode.BadRequest)
            else -> {
                val res= PostSource().getPostsByUserId(userId)
                when(res.isEmpty()){
                    true -> call.respond(HttpStatusCode.NotFound)
                    else -> call.respond(res)
                }
            }
        }

    }
    get("/subedPost/{userId}") { TODO() }

    post { TODO() }
    post("/subPost/{postId}") { TODO() }
    post("/subLocation/{locationId}") { TODO() }

    put { TODO() }
    put("/subPost/{postId}") { TODO() }
    put("/subLocation/{locationId}") { TODO() }

    delete { TODO() }
}