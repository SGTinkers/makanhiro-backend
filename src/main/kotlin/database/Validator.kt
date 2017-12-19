package database

import io.ktor.util.ValuesMap
import models.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class Validator {
    fun validatePost(unValidatedPost: ValuesMap, user: User): Post =
            Post(
                    Utils.sha256(user.facebookId + unValidatedPost["description"] + LocalDateTime.now()),
                    validateLocation(unValidatedPost),
                    validateTimestamp(unValidatedPost["expiryTime"]),
                    unValidatedPost["images"].toString().toImageList(),
                    validateDietary(unValidatedPost["dietary"].toString()),
                    unValidatedPost["description"].toString(),
                    FoodAvailability.valueOf(unValidatedPost["foodAvailability"].toString()),
                    Utils.timeStampNow(),
                    Utils.timeStampNow(),
                    user.userId
            )

    private fun validateLocation(unValidatedPost: ValuesMap): Location {
        val locationInt = try {
            unValidatedPost["locationId"]?.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            null
        }
        return when (locationInt) {
            null -> throw InvalidPostObject("LocationId cannot be null")
            else -> {
                val location = LocationSource().getLocationSourceById(locationInt)
                when (location) {
                    null -> throw InvalidPostObject("Invalid location")
                    else -> location
                }
            }
        }

    }

    private fun validateDietary(dietaryString: String?): Dietary? =
            if (dietaryString.isNullOrBlank()) null
                    else dietaryString?.let { Dietary.valueOf(it) }

    private fun validateTimestamp(dateTimeInString: String?): Timestamp =
            Timestamp(SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                    .parse(dateTimeInString).time)

    private fun String?.toImageList(): List<String>? {
        return when (this.isNullOrBlank()) {
            true -> null
            else -> ArrayList<String>(this
                    ?.split(","))
                    .map { c: String -> c.trim() }
                    .toList()
        }
    }

    class InvalidPostObject(msg: String) : Exception(msg)
}