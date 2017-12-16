package routes.authentication

import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.auth(path:String) = route("$path/auth"){
    post("/login"){

    }

    post("/register"){

    }
}

