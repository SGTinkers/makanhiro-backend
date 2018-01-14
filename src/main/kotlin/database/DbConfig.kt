package database

import java.sql.Connection
import java.sql.DriverManager

fun getDbConnection():Connection {
    val jdbcDriver = "com.mysql.cj.jdbc.Driver"
    val dbUrl= "jdbc:mysql://localhost/makanhiro?useLegacyDatetimeCode=false&serverTimezone=UTC"
    val user = "root"
    val password = "kek"

    try {
        Class.forName(jdbcDriver)
    }catch (e:ClassNotFoundException){
        e.printStackTrace()
    }

    return DriverManager.getConnection(dbUrl,user,password)
}

const val IMAGES_DIR = "public"
const val MAX_SIZE = 3
