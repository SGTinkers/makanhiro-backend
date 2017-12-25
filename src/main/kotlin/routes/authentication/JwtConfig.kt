package routes.authentication

import database.AuthSource
import io.jsonwebtoken.*
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.pipeline.PipelineContext
import io.ktor.request.header
import models.NotCompleted
import models.NotTested
import models.User
import java.util.*

object JwtConfig {
    private const val secret = "ASDNASKDNASDNASJKDNASKNDJNASNDASJKDNASKDNASDA" //Please change before compiling
    private const val issuer = "Msociety"
    private const val validityInMs: Long = 36_000_000 //10hrs

    fun parse(token: String): String = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
            .let({ it -> it["id"].toString() })

    fun makeToken(user: User): String = Jwts.builder()
            .setSubject("Authentication")
            .setIssuer(issuer)
            .claim("id", user.userId)
            .claim("email", user.email)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}

data class JwtObjForFrontEnd(val token: String,
                             val expiry: Date)

fun PipelineContext<Unit, ApplicationCall>.jwtAuth() {
    val token = call.request.header("Authorization")?.removePrefix("Bearer ") ?: return
    val userId = JwtConfig.parse(token)
    val user = AuthSource().getUserById(userId)
    if (user != null)
        call.attributes.put(User.key, user)
}


fun PipelineContext<*, ApplicationCall>.requireLogin(): User? = try {
    optionalLogin()
}catch (e:IllegalStateException){
    null
}

fun PipelineContext<*, ApplicationCall>.optionalLogin(): User? = call.attributes[User.key]