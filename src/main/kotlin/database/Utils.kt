package database

import models.Dietary
import models.FoodAvailability
import models.Post
import java.security.MessageDigest
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp

class Utils {
    companion object {
        /**
         * This function reduces lines for setting of null for preparedStatement
         * @param parameterIndex
         * @param t
         * @param ps
         */
        fun <T> setNullIfNull(parameterIndex: Int, t: T, ps: PreparedStatement) {
            when (t) {
                null -> ps.setNull(parameterIndex, java.sql.Types.NULL)
                is String -> {
                    if(t.isBlank())
                        ps.setString(parameterIndex, null)
                    else
                        ps.setString(parameterIndex, t)
                }
                is Dietary -> ps.setString(parameterIndex,t.toString())
                is Int -> ps.setInt(parameterIndex, t)
                is Timestamp -> ps.setTimestamp(parameterIndex, t)
                else -> throw NotImplementedError("This feature haven't been implemented")
            }
        }

        /**
         * Reduce code written for setting of timeStamp to now
         * @param parameterIndex
         * @param ps
         */
        fun setTimeStampNow(parameterIndex: Int, ps: PreparedStatement) {
            ps.setTimestamp(parameterIndex, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
        }

        /**
         * Reduces code written for mapping values from database results to Post Object
         * @param rs ResultSet
         * @return post Post Object
         */
        fun dbResToPostObject(rs: ResultSet): Post {
            val images = rs.getString("images")
            val imageToString = if (images != null) ArrayList<String>(images
                    .split(","))
                    .map { c: String -> c.trim() }
                    .toList() else null

            val location = LocationSource()
                    .getLocationSourceById(rs.getInt("locationId"))

            val dietary = rs.getString("dietary")
            val dietaryEnum: Dietary? = when (dietary) {
                null -> null
                else -> Dietary.valueOf(dietary)
            }

            return Post(
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
        }

        fun timeStampNow() = Timestamp(System.currentTimeMillis())

        fun sha512(input: String) = hashString("SHA-512", input)
        fun sha256(input: String) = hashString("SHA-256", input)
        fun sha1(input: String) = hashString("SHA-1", input)

        /**
         * You can add on more hashing algorithm above
         * @param hashAlgo
         * @param input
         * @return hashedString
         */
        private fun hashString(hashAlgo: String, input: String) =
                MessageDigest.getInstance(hashAlgo)
                        .digest(input.toByteArray())
                        .map { String.format("%02X", it).toLowerCase() }
                        .joinToString(separator = "")

    }
}