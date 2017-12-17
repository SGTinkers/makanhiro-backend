package routes.authentication

import io.jsonwebtoken.*
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
            .let { it["facebookId"].toString() }

    fun makeToken(user: User): String = Jwts.builder()
            .setSubject("Authentication")
            .setIssuer(issuer)
            .claim("id", user.facebookId)
            .claim("email", user.email)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}