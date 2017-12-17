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
    private const val secret = "mysupersecretsecretXD" //Please change before compiling
    private const val issuer = "Msociety"
    private const val validityInMs = 36_000_000 //10hrs

    fun parse(token:String):String = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJwt(token)
            .body
            .let({ it -> it["userId"].toString() })

    fun makeToken(user: User): String = Jwts.builder()
            .setId(user.userId)
            .setSubject("Authentication")
            .setIssuer(issuer)
            .claim("email", user.email)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}

data class JwtObjForFrontEnd(val token:String,
                             val expiry:Date)

@NotTested
fun PipelineContext<Unit,ApplicationCall>.jwtAuth() {
    val token = call.request.header("Authorization")?.removePrefix("Bearer ") ?: return
    val userId = JwtConfig.parse(token)
    val user = AuthSource().getUserById(userId)
    if(user != null)
        call.attributes.put(User.key,user)
}

@NotTested
fun PipelineContext<*, ApplicationCall>.requireLogin(): User = optionalLogin() ?:
        throw NotLoggedIn("Not Logged in")

@NotTested
@NotCompleted
fun PipelineContext<*, ApplicationCall>.optionalLogin(): User? = call.attributes[User.key]

class NotLoggedIn(var msg: String): Exception()