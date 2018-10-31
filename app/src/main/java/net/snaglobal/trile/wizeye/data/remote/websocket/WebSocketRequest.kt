package net.snaglobal.trile.wizeye.data.remote.websocket

import com.squareup.moshi.Json

/**
 * @author lmtri
 * @since Oct 26, 2018 at 11:32 AM
 */
data class WebSocketRequest(
        @field:Json(name = "msg")
        val message: String = "method",

        @field:Json(name = "id")
        val requestId: Int,

        @field:Json(name = "method")
        val method: String,

        @field:Json(name = "params")
        val params: WebSocketRequestParams? = null
)

data class WebSocketRequestParams(
        @field:Json(name = "query")
        val query: WebSocketRequestQuery
)

data class WebSocketRequestQuery(
        @field:Json(name = "PlayerTypeName")
        val playerTypeName: String = "",

        @field:Json(name = "Name")
        val name: String = ""
)