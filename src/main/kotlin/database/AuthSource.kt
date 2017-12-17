package database

import models.User
import java.sql.SQLException

class AuthSource {
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
}