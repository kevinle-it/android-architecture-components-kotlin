package net.snaglobal.trile.wizeye.api.model

import com.squareup.moshi.Json

/**
 * @author lmtri
 * @since Sep 28, 2018 at 1:53 PM
 */
data class LoginCredential(
        @field:Json(name = "domain")
        val domain: String,
        @field:Json(name = "username")
        val userId: String,
        @field:Json(name = "password")
        val password: String
)