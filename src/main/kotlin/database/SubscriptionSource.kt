package database

import models.NotCompleted
import models.NotTested
import models.Post

class SubscriptionSource {
    @NotCompleted
    @NotTested
    fun getUserSubscribedPost(userId: String): ArrayList<Post> { TODO() }

    @NotCompleted
    @NotTested
    fun subToPost(userId: String, postId: String): Boolean {
        TODO()
    }

    @NotCompleted
    @NotTested
    fun unsubFromPost(userId: String, postId: String): Boolean {
        TODO()
    }

    @NotCompleted
    @NotTested
    fun subToLocation(userId: String, locationId: Int): Boolean {
        TODO()
    }

    @NotCompleted
    @NotTested
    fun unsubFromLocation(userId: String, locationId: Int): Boolean {
        TODO()
    }
}