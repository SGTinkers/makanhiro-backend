package models

import java.sql.Timestamp

/**
 * Post Object
 * Please note that images are optional
 */
data class Post(val postId: String,
                val locationId:Int,
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
