package database

import models.Dietary
import models.FoodAvailability
import models.InvalidFileExtension
import models.Post
import java.security.MessageDigest
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp

class Utils {
    companion object {
        fun timeStampNow() = Timestamp(System.currentTimeMillis())

        fun sha512(input: String) = hashString("SHA-512", input)
        fun sha256(input: String) = hashString("SHA-256", input)
        fun sha1(input: String) = hashString("SHA-1", input)

        /**
         * You can add on more hashing algorithm above
         * @param hashAlgo
         * @param input
         * @return hashedString
         */
        private fun hashString(hashAlgo: String, input: String) =
                MessageDigest.getInstance(hashAlgo)
                        .digest(input.toByteArray())
                        .map { String.format("%02X", it).toLowerCase() }
                        .joinToString(separator = "")
    }
}

fun <T> PreparedStatement.setNullIfNull(parameterIndex: Int,t:T){
    when (t) {
        null -> this.setNull(parameterIndex, java.sql.Types.NULL)
        is String -> {
            if(t.isBlank())
                this.setString(parameterIndex, null)
            else
                this.setString(parameterIndex, t)
        }
        is Dietary -> this.setString(parameterIndex,t.toString())
        is Int -> this.setInt(parameterIndex, t)
        is Timestamp -> this.setTimestamp(parameterIndex, t)
        else -> throw NotImplementedError("This feature haven't been implemented")
    }
}

fun PreparedStatement.setTimeStampNow(parameterIndex: Int) =
        this.setTimestamp(parameterIndex, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))

fun ResultSet.toPostObject():Post{
    val images = this.getString("images")
    val imageToString = if (images != null) ArrayList<String>(images
            .split(","))
            .map { c: String -> c.trim() }
            .toList() else null

    val location = LocationSource()
            .getLocationSourceById(this.getInt("locationId"))

    val dietary = this.getString("dietary")
    val dietaryEnum: Dietary? = when (dietary) {
        null -> null
        else -> Dietary.valueOf(dietary)
    }
    return Post(
            this.getString("id"),
            location!!,
            this.getTimestamp("expiryTime"),
            imageToString,
            dietaryEnum,
            this.getString("description"),
            FoodAvailability.valueOf(this.getString("foodAvailability")),
            this.getTimestamp("createdAt"),
            this.getTimestamp("updatedAt"),
            this.getString("posterId"))
}

fun String?.toImageList(): List<String>? = when (this.isNullOrBlank()) {
    true -> null
    else -> ArrayList<String>(this
            ?.split(","))
            .map { c: String -> c.trim() }
            .toList()
}

fun String.isValidFileExt() = when(this){
    "jpeg" -> true
    "png" -> true
    "jpg" -> true
    else -> false

}

