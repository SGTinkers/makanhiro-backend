package database

import models.*
import java.sql.SQLException

class SubscriptionSource {

    /**
     * Get location that the user has subscribed
     * @param userId
     * @return locationSub
     */
    @NotCompleted
    @NotTested
    fun getLocationSub(userId: String):ArrayList<Int> {
        val sql = "SELECT * FROM locationSub WHERE userId = ?"
        val res = ArrayList<Int>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            val rs = ps.executeQuery()

            while (rs.next()){
                res.add(rs.getInt("locationId"))
            }

            ps.close()
            rs.close()
            conn.close()

            return res
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return res
    }

    /**
     * Get posts that the user has subscribed
     * @param userId
     * @return arList<PostSub>
     */
    fun getPostSub(userId: String):ArrayList<String> {
        val sql = "SELECT * FROM postsub WHERE userId = ?"
        val res = ArrayList<String>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,userId)
            val rs = ps.executeQuery()


            while (rs.next()){
                res.add(rs.getString("postId"))
            }

            ps.close()
            rs.close()
            conn.close()

            return res
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return res
    }

    /**
     * Subscribe to a post by postId
     * @param userId userId taken from JWT
     * @param postId postId taken from User
     * @return hasInserted
     */
    @TestedNotComprehensive
    fun subToPost(userId: String, postId: String): Boolean {
        val sql = "INSERT INTO postsub VALUES (?,?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,Utils.sha1(userId + postId))
            ps.setString(2,userId)
            ps.setString(3,postId)

            val rs = ps.executeUpdate()
            ps.close()
            conn.close()
            rs != 0
        }catch (e:SQLException){
            if(e.toString().contains("Duplicate"))
                throw DuplicateFound("$postId for user $userId has already been added!")
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
        val sql = "INSERT INTO locationsub VALUES (?,?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,Utils.sha1(userId  + locationId))
            ps.setString(2,userId)
            ps.setInt(3,locationId)

            val rs = ps.executeUpdate()

            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            if(e.toString().contains("Duplicate"))
                throw DuplicateFound("$locationId of user $userId has been added!")
            false
        }
    }

    /**
     * Unsubscribe to a location by locationId
     * @param userId userId taken from JWT
     * @param locationId locationId taken from User
     * @return hasDeleted
     */
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