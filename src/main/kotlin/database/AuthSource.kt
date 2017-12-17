package database

import models.User
import java.sql.SQLException

class AuthSource {
    fun registerUser(user:User):Boolean {
        val sql = "INSERT INTO user VALUES (?,?,?,?)"
        return try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1,user.userId)
            ps.setString(2,user.fullName)
            ps.setString(3,user.email)
            ps.setString(4,user.facebookId)
            val rs = ps.executeQuery()

            rs.next()
        }catch (e:SQLException){
            false
        }
    }
}