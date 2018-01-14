package database

import models.*
import java.sql.*
import kotlin.collections.ArrayList

/**
 * Please remember to use prepared statement to prevent SQL injection
 * If unsure how to use checkout this link
 * https://www.mkyong.com/jdbc/jdbc-preparestatement-example-select-list-of-the-records/
 */

class PostSource {

    /**
     * Instead of multiple url, have one url that have multiple query params which are
     * optional.For the SQL statement,leave it as it is unless there is another
     * faster way to execute the query
     * @param query PostQuery Object
     * @return arListOfPost ArrayList<Post>
     */
    fun getPosts(query: PostQuery): ArrayList<Post> {
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

            ps.setTimeStampNow(1)
            ps.setString(2, query.postId)
            ps.setString(3, query.postId)
            ps.setNullIfNull(4,query.locationId)
            ps.setNullIfNull(5,query.locationId)
            ps.setString(6, query.userId)
            ps.setString(7, query.userId)
            ps.setInt(8, query.limit)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val tempPost = rs.toPostObject()
                res.add(tempPost)
            }

            rs.close()
            ps.close()
            conn.close()

            return res
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return res
    }

    @TestedNotComprehensive
    @RequiresAuth
    fun createPost(post: Post): Boolean {
        val sql = "INSERT INTO post VALUES (?,?,?,?,?,?,?,?,?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1, post.postId)
            ps.setInt(2, post.location.locationId)
            ps.setTimestamp(3, post.expiryTime)
            ps.setNullIfNull(4,post.images?.joinToString(","))
            ps.setNullIfNull(5,post.dietary)
            ps.setString(6, post.description)
            ps.setString(7, post.foodAvailability.toString())
            ps.setTimestamp(8, post.createdAt)
            ps.setTimestamp(9, post.updatedAt)
            ps.setString(10, post.posterId)
            val rs = ps.executeUpdate()

            ps.close()
            conn.close()
            rs != 0
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }

    @NotCompleted
    @NotTested
    @MustBeSameUserAsPosterId
    fun editPost(post: Post): Boolean {
        val sql = "UPDATE post SET " +
                "locationId = ?, " +
                "expiryTime = ?," +
                "images = ?," +
                "dietary = ?," +
                "description = ?," +
                "foodAvailability = ?," +
                "updatedAt = ? "+
                "WHERE id = ? "+
                "AND posterId = ? "

        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setNullIfNull(1,post.location.locationId)
            ps.setTimestamp(2,post.expiryTime)
            ps.setNullIfNull(3,post.images?.joinToString(","))
            ps.setNullIfNull(4,post.dietary)
            ps.setString(5, post.description)
            ps.setString(6, post.foodAvailability.toString())
            ps.setTimestamp(7, post.updatedAt)
            ps.setString(8,post.postId)
            ps.setString(9, post.posterId)

            val rs = ps.executeUpdate()
            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }

    @MustBeSameUserAsPosterId
    fun deletePost(postId: String?,user:User): Boolean {
        val sql = "DELETE FROM post " +
                "WHERE id = ? AND posterId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setNullIfNull(1,postId)
            ps.setString(2,user.userId)
            val rs = ps.executeUpdate()
            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }
    }

}


