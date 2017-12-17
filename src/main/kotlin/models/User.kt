package models

import io.ktor.util.AttributeKey

data class User(val userId: String,
                val fullName: String,
                val email: String,
                val facebookId: String){
    companion object {
        val key = AttributeKey<User>("user")
    }
}

/**
 * name of vals must match the given Facebook response
 * so that we can convert string to Object using Gson without additional
 * hacks or workarounds
 * @param id facebookId
 * @param name user's name
 * @param email user's email address
 */
data class TempFacebookUser(val id: String,
                            val name: String,
                            val email: String)