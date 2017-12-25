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

fun Route.locationSub(path:String) = route("$path/locationSub") {
    get {
        val user = requireLogin()
        when(user){
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                try {
                    val res = SubscriptionSource().getLocationSub(user.userId)
                    call.respond(res)
                }catch (e:SQLException){
                    call.respond(HttpStatusCode.BadRequest,"Bad Request")
                }
            }
        }
    }
    post {
        val user = requireLogin()
        val locationId = call.receive<ValuesMap>()["locationId"]
        when(user) {
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                if(locationId == null)
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                try {
                    val res = SubscriptionSource().subToLocation(user.userId,locationId!!.toInt())
                    if(!res)
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                                INVALID_LOCATIONID))
                    else
                        call.respond("Successfully subbed to location: $locationId")

                }catch (e:SQLException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                }catch (e:NumberFormatException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                }catch (e:DuplicateFound){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            DUPLICATE_RECORDS_FOUND))
                }
            }
        }
    }
    delete {
        val user = requireLogin()
        val locationId = call.receive<ValuesMap>()["locationId"]
        when(user) {
            null -> call.respond(HttpStatusCode.Unauthorized,"401 Unauthorized")
            else -> {
                if(locationId == null)
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                try {
                    val res = SubscriptionSource().unsubFromLocation(user.userId,locationId!!.toInt())
                    if(!res)
                        call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                                INVALID_LOCATIONID))
                    else
                        call.respond("Successfully unsubbed to location: $locationId")

                }catch (e:SQLException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                }catch (e:NumberFormatException){
                    call.respond(HttpStatusCode.BadRequest,ErrorMsg("Bad Request. Invalid locationId",
                            INVALID_LOCATIONID))
                }
            }
        }
    }
}