package models

data class User(val userId: String,
                val fullName: String,
                val email: String,
                val facebookId: String)

data class TempFacebookUser(val id: String,
                            val name: String,
                            val email: String)