package database

import java.security.MessageDigest
import java.sql.PreparedStatement
import java.sql.Timestamp

class Utils {
    companion object {
        fun <T> setNullIfNull(index: Int,t:T,ps:PreparedStatement) {
            when(t){
                null -> ps.setNull(index, java.sql.Types.NULL)
                is String -> ps.setString(index,t)
                is Int -> ps.setInt(index,t)
                is Timestamp -> ps.setTimestamp(index,t)
                else -> throw NotImplementedError()
            }
        }
    }

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