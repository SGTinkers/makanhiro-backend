package models

data class User(val userId: String,
                val fullName: String,
                val email: String,
                val facebookId: String)

/**
 * name of vals must match the given Facebook resoponse
 * so that we can convert string to Object using Gson without additional
 * hacks or workarounds
 *
 * id in this case is facebookId
 */
data class TempFacebookUser(val id: String,
                            val name: String,
                            val email: String)