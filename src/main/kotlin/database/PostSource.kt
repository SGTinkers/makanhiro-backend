package database

import models.*
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


    fun getPosts(query:PostQuery): ArrayList<Post> {
        val res = ArrayList<Post>()
        val sql =
                "SELECT * FROM post " +
                        "WHERE expiryTime > ? " +
                        "AND foodAvailability != 'FINISHED' " +
                        "AND (? IS NULL or id = ?) " +
                        "AND (? IS NULL or locationId = ?) " +
                        "AND (? IS NULL or posterId = ?) " +
                        "ORDER BY createdAt LIMIT ?"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
            when(query.postId){
                null -> {
                    ps.setNull(2, java.sql.Types.NULL)
                    ps.setNull(3, java.sql.Types.NULL)
                }
                else -> {
                    ps.setString(2,query.postId)
                    ps.setString(3,query.postId)
                }
            }
            when(query.locationId){
                null -> {
                    ps.setNull(4, java.sql.Types.NULL)
                    ps.setNull(5, java.sql.Types.NULL)
                }
                else -> {
                    ps.setInt(4,query.locationId)
                    ps.setInt(5,query.locationId)
                }
            }
            ps.setString(6,query.userId)
            ps.setString(7,query.userId)
            ps.setInt(8,query.limit)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val images = rs.getString("images")
                val imageToString = if (images != null) ArrayList<String>(images
                        .split(","))
                        .map { c: String -> c.trim() }
                        .toList() else null
                val location = LocationSource()
                        .getLocationSourceById(rs.getInt("locationId"))
                val tempPost = Post(
                        rs.getString("id"),
                        location,
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

            rs.close()
            ps.close()
            conn.close()

            return res
        } catch (e: DbConnectionError) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return res
    }

    fun getPostById(id: String): Post? {
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND id = ?"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1, Date.valueOf(LocalDate.now()))
            ps.setString(2, id)
            val rs = ps.executeQuery()

            // For this one there is only expected result so dont need to iterate the resultset
            if (rs.next()) {
                val images = rs.getString("images")
                val imageToString = if (images != null) ArrayList<String>(images
                        .split(","))
                        .map { c: String -> c.trim() }
                        .toList() else null

                val location = LocationSource()
                        .getLocationSourceById(rs.getInt("locationId"))

                val post = Post(
                        rs.getString("id"),
                        location,
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        Dietary.valueOf(rs.getString("dietary")),
                        rs.getString("description"),
                        FoodAvailability.valueOf(rs.getString("foodAvailability")),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("posterId"))


                rs.close()
                ps.close()
                conn.close()

                return post
            }
        } catch (e: DbConnectionError) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun getPostsByLocationId(locationId: Int): ArrayList<Post> {
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND locationId = ? AND foodAvailability != 'FINISHED'"
        val res = ArrayList<Post>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1, Date.valueOf(LocalDate.now()))
            ps.setInt(2, locationId)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val images = rs.getString("images")
                val imageToString = if (images != null) ArrayList<String>(images
                        .split(","))
                        .map { c: String -> c.trim() }
                        .toList() else null

                val location = LocationSource()
                        .getLocationSourceById(rs.getInt("locationId"))

                val temp = Post(
                        rs.getString("id"),
                        location,
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

            rs.close()
            ps.close()
            conn.close()
        } catch (e: DbConnectionError) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return res
    }

    fun getPostsByUserId(userId: String): ArrayList<Post> {
        val sql = "SELECT * FROM post WHERE expiryTime > ? AND posterId = ? AND foodAvailability != 'FINISHED'"
        val res = ArrayList<Post>()
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setDate(1, Date.valueOf(LocalDate.now()))
            ps.setString(2, userId)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val images = rs.getString("images")
                val imageToString = if (images != null) ArrayList<String>(images
                        .split(","))
                        .map { c: String -> c.trim() }
                        .toList() else null

                val location = LocationSource()
                        .getLocationSourceById(rs.getInt("locationId"))

                val temp = Post(
                        rs.getString("id"),
                        location,
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
            rs.close()
            ps.close()
            conn.close()
        } catch (e: DbConnectionError) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return res
    }

    @NotCompleted
    @NotTested
    fun createPost(post: Post): Boolean {
        val sql = "INSERT INTO post VALUES (?,?,?,?,?,?,?,?,?,?)"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1, post.postId)
            post.location?.locationId?.let { ps.setInt(2, it) }
            ps.setTimestamp(3, post.expiryTime)
            ps.setString(4, post.images?.joinToString(","))
            ps.setString(5, post.dietary.toString())
            ps.setString(6, post.description)
            ps.setString(7, post.foodAvailability.toString())
            ps.setTimestamp(8, post.createdAt)
            ps.setTimestamp(9, post.updatedAt)
            ps.setString(10, post.posterId)
            val rs = ps.executeUpdate()

            if (rs == 0) return false

            return true
        } catch (e: DbConnectionError) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    @NotCompleted
    @NotTested
    fun editPost(post: Post): Boolean {
        TODO()
    }

    @NotCompleted
    @NotTested
    fun deletePost(postId: String): Boolean {
        TODO()
    }


}