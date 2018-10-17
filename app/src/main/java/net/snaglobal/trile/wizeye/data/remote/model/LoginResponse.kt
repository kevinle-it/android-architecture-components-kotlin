package net.snaglobal.trile.wizeye.data.remote.model

import com.squareup.moshi.Json

/**
 * @author lmtri
 * @since Sep 28, 2018 at 1:05 PM
 */
data class LoginResponse(
        @field:Json(name = "success")
        val successfulStatus: Boolean,
        @field:Json(name = "message")
        val message: String,
        @field:Json(name = "token")
        val token: String
)