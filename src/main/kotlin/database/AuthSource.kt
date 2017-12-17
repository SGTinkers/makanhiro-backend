package database

import models.User
import java.sql.SQLException

class AuthSource {

    /**
     * Adds User object into the database
     * @param user User Object
     * @return rowChanged Number of record that has been added. Should be 1 or 0
     */
    fun registerUser(user:User):Int {
        val sql = "INSERT INTO user VALUES (?,?,?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,user.userId)
            ps.setString(2,user.fullName)
            ps.setString(3,user.email)
            ps.setString(4,user.facebookId)
            val rs = ps.executeUpdate()
            rs
        }catch (e:SQLException){
            0
        }
    }

    /**
     * Similar to getUserByFbId() except that this function doesn't create a User Object
     * @param facebookId facebookId is to be obtained from clientSide. It differs if appId & appSecret is different
     * @return isExist Boolean Value of existence of record in database
     */
    fun isUserExistInDb(facebookId:String):Boolean {
        val sql = "SELECT * FROM user WHERE facebookId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,facebookId)

            val rs = ps.executeQuery()
            rs.next()
        }catch (e:SQLException){
            false
        }
    }

    /**
     * Gets User Object by Facebook Id
     * @param facebookId facebookId is to be obtained from clientSide. It differs if appId & appSecret is different
     * @return user User Object or null
     */
    fun getUserByFbId(facebookId:String):User? {
        val sql = "SELECT * FROM user WHERE facebookId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,facebookId)
            val rs = ps.executeQuery()
            val user = if(rs.next()) User(
                    rs.getString("userId"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("facebookId"))
            else null

            return user
        }catch (e:SQLException){
            null
        }
    }

    /**
     * Similar to the other, except this finds user by userId instead of facebookId
     * @param id userId is SHA256 of name + email + facebookId
     * @return user User Object or null
     */
    fun getUserById(id:String):User?{
        val sql = "SELECT * FROM user WHERE userId = ?"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,id)
            val rs = ps.executeQuery()
            val user = if(rs.next()) User(
                    rs.getString("userId"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("facebookId"))
            else null

            return user
        }catch (e:SQLException){
            null
        }
    }
}