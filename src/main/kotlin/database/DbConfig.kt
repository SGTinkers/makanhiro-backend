package database

import java.sql.Connection
import java.sql.DriverManager

fun getDbConnection():Connection {
    val jdbcDriver = "com.mysql.cj.jdbc.Driver"
    val dbUrl= "jdbc:mysql://localhost/makanhiro?useLegacyDatetimeCode=false&serverTimezone=UTC"
    val user = "root"
    val password = "12345"

    try {
        Class.forName(jdbcDriver)
    }catch (e:ClassNotFoundException){
        e.printStackTrace()
    }

    return DriverManager.getConnection(dbUrl,user,password)?:
            throw DbConnectionError("There is an error connecting to the database.Please " +
                    "check config / whether your instance of database is running ☺")
}

class DbConnectionError(var msg: String): Exception()
class DbEntryNotFound(var msg: String) : Exception()