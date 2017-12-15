package models

import java.sql.Date

/**
 * Post Object
 * Please note that images are optional
 */
data class Post(val postId: String,
                val locationId:Int,
                val expiryTime: Date,
                val images: List<String>?,
                val dietary: Dietary,
                val description: String,
                val foodAvailability:FoodAvailability,
                val createdAt: Date,
                val updatedAt: Date,
                val deletedAt: Date?,
                val posterId: String)


enum class Dietary {
    HALAL, VEGETARIAN
}

enum class FoodAvailability {
    ABUNDANT,FINISHING,FINISHED
}
