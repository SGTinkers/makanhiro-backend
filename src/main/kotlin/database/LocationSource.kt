package database

import models.Location
import models.LocationName
import java.sql.SQLException

class LocationSource {
    fun getLocationSourceById(id: Int):Location? {
        val sql = "SELECT * FROM  location WHERE locationId = ?"
        try {
            val conn = getDbConnection()
            val ps = conn.prepareStatement(sql)
            ps.setInt(1,id)
            val rs = ps.executeQuery()
            if(rs.next()){
                val location = Location(
                        rs.getInt("locationId"),
                        LocationName.valueOf(rs.getString("locationName")),
                        rs.getString("locationDetails"))

                rs.close()
                ps.close()
                conn.close()
                return location
            }

        }catch (e:DbConnectionError){
            e.printStackTrace()
        }catch (e:SQLException){
            e.printStackTrace()
        }
        return null
    }
}