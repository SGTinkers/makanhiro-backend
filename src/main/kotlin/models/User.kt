package models

data class User(val userId: Int,
                val fullName: String,
                val email: String,
                val facebookId: String)