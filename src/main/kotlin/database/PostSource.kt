package database

import io.ktor.util.ValuesMap
import models.*
import java.sql.*
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime.now
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
    @TestedNotComprehensive
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

            Utils.setTimeStampNow(1,ps)
            ps.setString(2, query.postId)
            ps.setString(3, query.postId)
            Utils.setNullIfNull(4,query.locationId,ps)
            Utils.setNullIfNull(5,query.locationId,ps)
            ps.setString(6, query.userId)
            ps.setString(7, query.userId)
            ps.setInt(8, query.limit)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val tempPost = Utils.dbResToPostObject(rs)
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
            Utils.setNullIfNull(4,post.images,ps)
            Utils.setNullIfNull(5,post.dietary.toString(),ps)
            ps.setString(6, post.description)
            ps.setString(7, post.foodAvailability.toString())
            ps.setTimestamp(8, post.createdAt)
            ps.setTimestamp(9, post.updatedAt)
            ps.setString(10, post.posterId)
            val rs = ps.executeUpdate()

            ps.close()
            conn.close()
            (rs != 0)
        } catch (e: SQLException) {
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
                "AND posterI = ? "
        /*return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            Utils.setNullIfNull(1,post.location.locationId,ps)
            ps.setTimestamp(2,post.expiryTime)
            Utils.setNullIfNull(3,post.images,ps)
            Utils.setNullIfNull(4,post.dietary.toString(),ps)
            ps.setString(6, post.description)
            ps.setString(7, post.foodAvailability.toString())
            ps.setTimestamp(8, post.updatedAt)
            ps.setString(9,post.postId)
            ps.setString(10, post.posterId)

            val rs = ps.executeUpdate()
            ps.close()
            conn.close()

            rs != 0
        }catch (e:SQLException){
            false
        }*/
        TODO("Musa send help I have no idea how to optimise this")

    }

    @TestedNotComprehensive
    @MustBeSameUserAsPosterId
    fun deletePost(postId: String?,user:User): Boolean {
        val sql = "DELETE FROM post " +
                "WHERE id = ? AND posterId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            Utils.setNullIfNull(1,postId,ps)
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


