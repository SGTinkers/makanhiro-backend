package database

import models.NotCompleted
import models.NotTested
import models.Post
import models.TestedNotComprehensive
import java.sql.SQLException

class SubscriptionSource {
    @NotCompleted
    @NotTested
    fun getUserSubscribedPost(userId: String): ArrayList<Post> { TODO() }

    /**
     * Subscribe to a post by postId
     * @param userId userId taken from JWT
     * @param postId postId taken from User
     * @return hasInserted
     */
    @TestedNotComprehensive
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

    /**
     * Unsubscribe from Post by postId
     * @param userId userId taken from JWT
     * @param postId postId taken from User
     * @return hasDeleted
     */
    @TestedNotComprehensive
    fun unsubFromPost(userId: String, postId: String): Boolean {
        val sql = "DELETE FROM postsub WHERE userId = ? AND postId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            ps.setString(2,postId)

            val rs = ps.executeUpdate()

            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    /**
     * Subscribe to a location by locationId
     * @param userId userId taken from JWT
     * @param locationId locationId taken from User
     * @return hasInserted
     */
    @TestedNotComprehensive
    fun subToLocation(userId: String, locationId: Int): Boolean {
        val sql = "INSERT INTO locationsub VALUES (?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setInt(1,locationId)
            ps.setString(2,userId)

            val rs = ps.executeUpdate()

            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    @TestedNotComprehensive
    fun unsubFromLocation(userId: String, locationId: Int): Boolean {
        val sql = "DELETE FROM locationsub WHERE userId = ? AND locationId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            ps.setInt(2,locationId)

            val rs = ps.executeUpdate()

            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }
}