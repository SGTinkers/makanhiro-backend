package routes

import database.PostSource
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*


fun Route.post(path: String) = route("$path/post"){
    get { call.respond(PostSource().getPosts()) }

    get("/{postId}") {
        val id = call.parameters["postId"]
        when(id) {
            null -> call.respondText("No post such found!", ContentType.Text.JavaScript)
            else -> when(PostSource().getPostById(id)) {
                null -> call.respondText("No post such found!", ContentType.Text.JavaScript)
                else -> PostSource().getPostById(id)?.let { it1 -> call.respond(it1) }
            }
        }
    }

    get("/location/{locationId}"){
        val id = try { call.parameters["locationId"]?.toInt() } catch (e:NumberFormatException) {
            null
        }
        when(id) {
            null -> call.respondText("""Invalid locationId Format
                |Format should be: integer
                |Example: 3
            """.trimMargin(), ContentType.Text.JavaScript)
            else -> {
                val res= PostSource().getPostsByLocationId(id)
                when(res.isEmpty()) {
                    true -> call.respondText("No post with locationId: $id found!", ContentType.Text.JavaScript)
                    else -> res.let { it1 -> call.respond(it1) }
                }
            }
        }
    }

    get("/user/{userId}"){ TODO() }
    get("/subedPost/{userId}") { TODO() }

    post { TODO() }
    post("/subPost/{postId}") { TODO() }
    post("/subLocation/{locationId}") { TODO() }

    put { TODO() }
    put("/subPost/{postId}") { TODO() }
    put("/subLocation/{locationId}") { TODO() }

    delete { TODO() }
}