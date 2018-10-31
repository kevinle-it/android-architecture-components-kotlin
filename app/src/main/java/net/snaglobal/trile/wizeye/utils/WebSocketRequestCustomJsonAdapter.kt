package net.snaglobal.trile.wizeye.utils

import com.squareup.moshi.*
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestParams
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestQuery

/**
 * Custom Moshi Json Adapter used to convert JSON Keys of 'null' values into EMPTY OBJECT when
 * parsing WebSocketRequest object into a JSON String.
 *
 * Example: "params": null -> "params": {}
 *
 * This adapter also ignore Keys of EMPTY String Values in WebSocketRequestQuery object.
 *
 * Example: "query": {"PlayerTypeName": "video", "Name": ""} -> "query": {"PlayerTypeName": "video"}
 *
 * @author lmtri
 * @since Oct 31, 2018 at 9:10 AM
 */
class WebSocketRequestCustomJsonAdapter {
    companion object {
        val WEB_SOCKET_REQUEST_NAMES: JsonReader.Options = JsonReader.Options.of(
                "msg", "id", "method", "params"
        )
        val WEB_SOCKET_REQUEST_PARAMS_NAMES: JsonReader.Options = JsonReader.Options.of(
                "query"
        )
        val WEB_SOCKET_REQUEST_QUERY_NAMES: JsonReader.Options = JsonReader.Options.of(
                "PlayerTypeName", "Name"
        )
    }

    @Throws(JsonDataException::class)
    @FromJson
    fun fromJson(jsonReader: JsonReader): WebSocketRequest {
        return jsonReader.run {
            var message = ""
            var requestId = -1
            var method = ""
            var params: WebSocketRequestParams? = null

            beginObject()   // WebSocketRequest
            while (hasNext()) {
                when (selectName(WEB_SOCKET_REQUEST_NAMES)) {
                    0 -> message = nextString()
                    1 -> requestId = nextInt()
                    2 -> method = nextString()
                    3 -> {
                        var query: WebSocketRequestQuery? = null

                        beginObject()   // WebSocketRequestParams
                        while (hasNext()) {
                            when (selectName(WEB_SOCKET_REQUEST_PARAMS_NAMES)) {
                                0 -> {
                                    var playerTypeName = ""
                                    var name = ""

                                    beginObject()   // WebSocketRequestQuery
                                    while (hasNext()) {
                                        when (selectName(WEB_SOCKET_REQUEST_QUERY_NAMES)) {
                                            0 -> playerTypeName = nextString()
                                            1 -> name = nextString()
                                            else -> skipValue()
                                        }
                                    }
                                    endObject() // WebSocketRequestQuery

                                    query = WebSocketRequestQuery(playerTypeName, name)
                                }
                                else -> skipValue()
                            }
                        }
                        endObject() // WebSocketRequestParams

                        query?.let {
                            params = WebSocketRequestParams(it)
                        }
                    }
                    else -> skipValue()
                }
            }
            endObject() // WebSocketRequest

            if (message.isEmpty() || requestId == -1 || method.isEmpty()) {
                throw JsonDataException("Missing required field")
            }
            WebSocketRequest(
                    message = message,
                    requestId = requestId,
                    method = method,
                    params = params
            )
        }
    }

    @Throws(JsonDataException::class)
    @ToJson
    fun toJson(jsonWriter: JsonWriter, wsRequest: WebSocketRequest) {
        jsonWriter?.apply {
            beginObject()   // WebSocketRequest
                    ?.apply {
                        name("msg").value(wsRequest.message)
                        name("id").value(wsRequest.requestId)
                        name("method").value(wsRequest.method)
                        name("params")
                        wsRequest.params?.also {
                            beginObject()   // WebSocketRequestParams
                                    ?.apply {
                                        name("query")
                                        beginObject()   // WebSocketRequestQuery
                                                ?.apply {
                                                    if (it.query.playerTypeName.isNotEmpty()) {
                                                        name("PlayerTypeName").value(it.query.playerTypeName)
                                                    } else {
                                                        name("Name").value(it.query.name)
                                                    }
                                                }
                                        endObject() // WebSocketRequestQuery
                                    }
                            endObject() // WebSocketRequestParams
                        } ?: kotlin.run {
                            // If wsRequest.params == null
                            // Write EMPTY OBJECT as "params": {} INSTEAD OF "params": null
                            beginObject()
                            endObject()
                        }
                    }
            endObject() // WebSocketRequest
        }
    }
}