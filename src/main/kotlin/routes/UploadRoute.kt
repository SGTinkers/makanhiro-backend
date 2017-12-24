package routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.PartData
import io.ktor.request.forEachPart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import routes.authentication.requireLogin
import java.io.File

fun Route.images(path:String) = route("$path/images") {
    get {}
    post {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                val multipart = call.receiveMultipart()

                multipart.forEachPart {
                    if(it is PartData.FormItem) {
                        if(it.partName == "title")
                            call.respond(it.value)
                    }else if (it is PartData.FileItem){
                        val ext = File(it.originalFileName).extension
                        val imageFile = File("C:\\Users\\Budi Syahiddin\\Desktop\\test","xd.$ext")
                        it.streamProvider().use { its -> imageFile.outputStream().buffered().use {
                            its.copyTo(it)
                        }}
                    }
                }
            }
        }
    }
    put {}
    delete {}
}