package net.snaglobal.trile.wizeye.data.remote.websocket

import com.squareup.moshi.Json

/**
 * @author lmtri
 * @since Oct 26, 2018 at 4:14 PM
 */
data class WebSocketResponse(
        @field:Json(name = "msg")
        val message: String,

        @field:Json(name = "id")
        val requestId: Int,

        @field:Json(name = "result")
        val result: WebSocketResponseResult
)

data class WebSocketResponseResult(
        @field:Json(name = "body")
        val body: Any
)