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
     * optional.For the SQL statement,leave it as it is as the it will get optimised
     * by the parser
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
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
            ps.setString(2, query.postId)
            ps.setString(3, query.postId)
            when (query.locationId) {
                null -> {
                    ps.setNull(4, java.sql.Types.NULL)
                    ps.setNull(5, java.sql.Types.NULL)
                }
                else -> {
                    ps.setInt(4, query.locationId)
                    ps.setInt(5, query.locationId)
                }
            }
            ps.setString(6, query.userId)
            ps.setString(7, query.userId)
            ps.setInt(8, query.limit)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val images = rs.getString("images")
                val imageToString = if (images != null) ArrayList<String>(images
                        .split(","))
                        .map { c: String -> c.trim() }
                        .toList() else null

                val location = LocationSource()
                        .getLocationSourceById(rs.getInt("locationId"))

                val dietary = rs.getString("dietary")
                val dietaryEnum:Dietary? = when(dietary){
                    null -> null
                    else -> Dietary.valueOf(dietary)
                }


                val tempPost = Post(
                        rs.getString("id"),
                        location!!,
                        rs.getTimestamp("expiryTime"),
                        imageToString,
                        dietaryEnum,
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
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return res
    }

    @NotCompleted
    @TestedNotComprehensive
    @RequiresAuth
    fun createPost(post: Post): Boolean {
        val sql = "INSERT INTO post VALUES (?,?,?,?,?,?,?,?,?,?)"
        val images = post.images
        val dietary = post.dietary
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1, post.postId)
            ps.setInt(2, post.location.locationId)
            ps.setTimestamp(3, post.expiryTime)
            when(images){
                null -> ps.setNull(4, java.sql.Types.NULL)
                else -> ps.setString(4, post.images.joinToString(","))
            }
            when(dietary){
                null -> ps.setNull(5, java.sql.Types.NULL)
                else -> ps.setString(5, post.dietary.toString())
            }
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
        TODO()
    }

    @NotCompleted
    @NotTested
    @MustBeSameUserAsPosterId
    fun deletePost(postId: String): Boolean {
        TODO()
    }

}


