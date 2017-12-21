package database

import models.NotCompleted
import models.NotTested
import models.Post
import java.sql.SQLException

class SubscriptionSource {
    @NotCompleted
    @NotTested
    fun getUserSubscribedPost(userId: String): ArrayList<Post> { TODO() }

    @NotCompleted
    @NotTested
    fun subToPost(userId: String, postId: String): Boolean {
        val sql = "INSERT INTO postsub VALUES (?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,postId)
            ps.setString(2,userId)

            val rs = ps.executeUpdate()
            ps.close()
            conn.close()
            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    @NotTested
    fun unsubFromPost(userId: String, postId: String): Boolean {
        val sql = "DELETE FROM postsub WHERE userId = ? AND postId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            ps.setString(2,postId)

            val rs = ps.executeUpdate()
            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    @NotTested
    fun subToLocation(userId: String, locationId: String): Boolean {
        val sql = "INSERT INTO locationsub VALUES (?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            ps.setString(2,locationId)

            val rs = ps.executeUpdate()
            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    @NotCompleted
    @NotTested
    fun unsubFromLocation(userId: String, locationId: String): Boolean {
        val sql = "DELETE FROM locationsub WHERE userId = ? AND locationId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            ps.setString(2,locationId)

            val rs = ps.executeUpdate()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }
}