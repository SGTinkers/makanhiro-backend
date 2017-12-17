package routes.authentication


import com.google.gson.Gson
import database.AuthSource
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import models.TempFacebookUser
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import models.User
import java.io.IOException
import java.security.MessageDigest


fun Route.auth(path:String) = route("$path/auth"){
    get("/login/{facebookId}"){
        val test = call.parameters["facebookId"]?.let { it1 -> validateWithFacebook(it1) }
        test?.let { it1 -> call.respond(it1) }
    }

}

fun validateWithFacebook(accessToken:String): TempFacebookUser? {
    return try {
        val url = "https://graph.facebook.com/me?" +
                "fields=id,name,email&access_token=$accessToken"

        val conn = URL(url).openConnection() as HttpURLConnection
        val rd = BufferedReader(InputStreamReader(conn.inputStream))
        val line = rd.readLine()

        val tempUser = Gson().fromJson(line,TempFacebookUser::class.java)

        rd.close()
        tempUser
    }catch (e:IOException){
        null
    }
}

fun register(tempFacebookUser: TempFacebookUser):Boolean{
    val toBeHashed = tempFacebookUser.name +
            tempFacebookUser.email +
            tempFacebookUser.id

    val hash = MessageDigest.getInstance("SHA-256")
            .digest(toBeHashed.toByteArray())
    val user = User(
            hash.toString(),
            tempFacebookUser.name,
            tempFacebookUser.email,
            tempFacebookUser.id)

    return AuthSource().registerUser(user)
}
