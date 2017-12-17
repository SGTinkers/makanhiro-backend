package models

import io.ktor.util.ValuesMap
import java.sql.Timestamp

data class PostQuery(val postId: String?,
                     val locationId: Int?,
                     val userId: String?,
                     val limit: Int) {
    companion object {
        fun fromParams(map: ValuesMap) = PostQuery(
                postId = if(!map["postId"].isNullOrBlank()) map["postId"] else null,
                locationId = map["locationId"]?.toInt(),
                userId = if(!map["userId"].isNullOrBlank()) map["userId"] else null,
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
