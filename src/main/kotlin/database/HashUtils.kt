package database

import java.security.MessageDigest

class HashUtil {
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