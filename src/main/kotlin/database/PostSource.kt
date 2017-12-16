package database

import models.Dietary
import models.FoodAvailability
import models.Post
import java.sql.*
import java.sql.Date
import java.time.LocalDate
import kotlin.collections.ArrayList

/**
 * Please remember to use prepared statement to prevent any form of SQL injection
 * If unsure how to use checkout this link
 * https://www.mkyong.com/jdbc/jdbc-preparestatement-example-select-list-of-the-records/
 */

class PostSource {
    /**
     * Status: Completed
     * Test: Working
     */
    fun getPosts():ArrayList<Post>{
        val res = ArrayList<Post>()
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND foodAvailability != 'FINISHED'"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1,Date.valueOf(LocalDate.now()))
            val rs = ps.executeQuery()

            while(rs.next()){
                val images = rs.getString("images")
                val imageToString = if(images !=null) ArrayList<String>(images
                        .split(","))
                        .map { c:String -> c.trim() }
                        .toList() else null

                val tempPost = Post(
                        rs.getString("id"),
                        rs.getInt("locationId"),
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        Dietary.valueOf(rs.getString("dietary")),
                        rs.getString("description"),
                        FoodAvailability.valueOf(rs.getString("foodAvailability")),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("posterId"))

                res.add(tempPost)
            }

            return res
        }catch (e:DbConnectionError){
            e.printStackTrace()
        }
        catch (e:SQLException){
            e.printStackTrace()
        }
        return res
    }
    /**
     * Status: Completed
     * Test: Working
     */
    fun getPostById(id:String):Post?{
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND id = ?"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1,Date.valueOf(LocalDate.now()))
            ps.setString(2, id)
            val rs = ps.executeQuery()

            // For this one there is only expected result so dont need to iterate the resultset
            if (rs.next()){
                val images = rs.getString("images")
                val imageToString = if(images !=null) ArrayList<String>(images
                        .split(","))
                        .map { c:String -> c.trim() }
                        .toList() else null

                return Post(
                        rs.getString("id"),
                        rs.getInt("locationId"),
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        Dietary.valueOf(rs.getString("dietary")),
                        rs.getString("description"),
                        FoodAvailability.valueOf(rs.getString("foodAvailability")),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("posterId"))
            }
        }catch (e:DbConnectionError){
            e.printStackTrace()
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return null
    }
    /**
     * Status: Completed
     * Test: Working
     */
    fun getPostsByLocationId(locationId:Int):ArrayList<Post>{
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND locationId = ? AND foodAvailability != 'FINISHED'"
        val res = ArrayList<Post>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1,Date.valueOf(LocalDate.now()))
            ps.setInt(2,locationId)
            val rs = ps.executeQuery()

            while (rs.next()){
                val images = rs.getString("images")
                val imageToString = if(images !=null) ArrayList<String>(images
                        .split(","))
                        .map { c:String -> c.trim() }
                        .toList() else null

                val temp = Post(
                        rs.getString("id"),
                        rs.getInt("locationId"),
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        Dietary.valueOf(rs.getString("dietary")),
                        rs.getString("description"),
                        FoodAvailability.valueOf(rs.getString("foodAvailability")),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("posterId"))

                res.add(temp)
            }
        }catch (e:DbConnectionError){
            e.printStackTrace()
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return res
    }
    /**
     * Status: Completed
     * Test: Working
     */
    fun getPostsByUserId(userId:String):ArrayList<Post>{
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND posterId = ? AND foodAvailability != 'FINISHED'"
        val res = ArrayList<Post>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1,Date.valueOf(LocalDate.now()))
            ps.setString(2,userId)
            val rs = ps.executeQuery()

            while (rs.next()){
                val images = rs.getString("images")
                val imageToString = if(images !=null) ArrayList<String>(images
                        .split(","))
                        .map { c:String -> c.trim() }
                        .toList() else null

                val temp = Post(
                        rs.getString("id"),
                        rs.getInt("locationId"),
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        Dietary.valueOf(rs.getString("dietary")),
                        rs.getString("description"),
                        FoodAvailability.valueOf(rs.getString("foodAvailability")),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("posterId"))

                res.add(temp)
            }
        }catch (e:DbConnectionError){
            e.printStackTrace()
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return res
    }
    fun getUserSubscribedPost(userId: String):ArrayList<Post>{
        TODO("Musa send help for this I not sure for the DB operations")
    }
    ///////////////
    //End of GET//
    //////////////
    fun createPost(post:Post):Boolean {
        val sql = "INSERT INTO post VALUES (?,?,?,?,?,?,?,?,?,?)"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,post.postId)
            ps.setInt(2,post.locationId)
            ps.setTimestamp(3,post.expiryTime)
            ps.setString(4,post.images?.joinToString(","))
            ps.setString(5,post.dietary.toString())
            ps.setString(6,post.description)
            ps.setString(7,post.foodAvailability.toString())
            ps.setTimestamp(8,post.createdAt)
            ps.setTimestamp(9,post.updatedAt)
            ps.setString(10,post.posterId)
            val rs = ps.executeUpdate()

            if (rs == 0) return false

            return true
        }catch (e:DbConnectionError){
            e.printStackTrace()
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return false
    }
    fun editPost(post:Post):Boolean {
        TODO()
    }
    fun deletePost(postId:String):Boolean {
        TODO()
    }
    fun subToPost(userId:String,postId:String):Boolean {
        TODO()
    }
    fun unsubFromPost(userId:String,postId:String):Boolean {
        TODO()
    }
    fun subToLocation(userId: String,locationId: Int):Boolean {
        TODO()
    }
    fun unsubFromLocation(userId: String,locationId: Int):Boolean {
        TODO()
    }
}