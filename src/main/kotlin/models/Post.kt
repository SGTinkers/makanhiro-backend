package models

import io.ktor.util.ValuesMap
import java.sql.Timestamp

data class PostQuery(val postId: String?,
                     val locationId: Int?,
                     val userId: String?,
                     val limit: Int) {
    companion object {
        fun fromParams(map: ValuesMap) = PostQuery(
                postId = map["postId"],
                locationId = map["locationId"]?.toInt(),
                userId = map["userId"],
                limit = map["limit"]?.toInt() ?:20
        )
    }
}

/**
 * Post Object
 * Please note that images are optional
 */
data class Post(val postId: String,
                val location:Location?,
                val expiryTime: Timestamp,
                val images: List<String>?,
                val dietary: Dietary,
                val description: String,
                val foodAvailability:FoodAvailability,
                val createdAt: Timestamp,
                val updatedAt: Timestamp,
                val posterId: String)


enum class Dietary {
    HALAL, VEGETARIAN
}

enum class FoodAvailability {
    ABUNDANT,FINISHING,FINISHED
}
