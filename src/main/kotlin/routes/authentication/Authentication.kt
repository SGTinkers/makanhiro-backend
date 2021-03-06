package routes.authentication


import com.google.gson.Gson
import database.AuthSource
import database.Utils
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import models.*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException


fun Route.auth(path: String) = route("$path/auth") {
    get("/login") {
        val fbAcToken = call.parameters["fbToken"]
        val jwt = fbAcToken?.let { it1 -> validateWithFacebook(it1) }
        when (jwt) {
            null -> call.respond(HttpStatusCode.Unauthorized)
            else -> call.respond(JwtObjForFrontEnd(jwt[0], jwt[1]))
        }
    }
}

fun validateWithFacebook(accessToken: String): ArrayList<String>? {
    return try {
        val url = "https://graph.facebook.com/me?" +
                "fields=id,name,email&access_token=$accessToken"

        val conn = URL(url).openConnection() as HttpURLConnection
        val rd = BufferedReader(InputStreamReader(conn.inputStream))
        val line = rd.readLine()

        val tempUser = Gson().fromJson(line, TempFacebookUser::class.java)
        rd.close()
        val hasRegistered = AuthSource().isUserExistInDb(tempUser.id)
        return if (!hasRegistered)
            when (register(tempUser)) {
                0 -> null
                else -> {
                    val arList = ArrayList<String>()
                    AuthSource().getUserByFbId(tempUser.id)?.let(JwtConfig::makeToken)?.let { arList.add(it) }
                    arList.add(tempUser.id)

                    arList
                }
            }
        else {
            val arList = ArrayList<String>()
            AuthSource().getUserByFbId(tempUser.id)?.let(JwtConfig::makeToken)?.let { arList.add(it) }
            arList.add(tempUser.id)

            arList
        }


    } catch (e: IOException) {
        null
    }
}

/**
 * This function creates User object from TempFacebookUser
 * And inserts it into database via AuthSource().registerUser(user)
 * which takes in a User object
 * @param tempFacebookUser tempFacebookUser is just a placeholder needed to construct an object from the HTTP GET to facebook
 * @return noOfRowsChanged
 */
@TestedNotComprehensive
fun register(tempFacebookUser: TempFacebookUser): Int = AuthSource().registerUser(User(
        Utils.sha256(tempFacebookUser.name +
                tempFacebookUser.email +
                tempFacebookUser.id),
        tempFacebookUser.name,
        tempFacebookUser.email,
        tempFacebookUser.id))


