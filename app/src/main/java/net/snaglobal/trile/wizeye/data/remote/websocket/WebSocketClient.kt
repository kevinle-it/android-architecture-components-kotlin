package net.snaglobal.trile.wizeye.data.remote.websocket

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient.onSentMessageCallbacks
import net.snaglobal.trile.wizeye.utils.SingletonHolder
import net.snaglobal.trile.wizeye.utils.WebSocketRequestCustomJsonAdapter
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * @author lmtri
 * @since Oct 25, 2018 at 1:33 PM
 */
object WebSocketClient : SingletonHolder<WebSocket, String>({ serverUrl: String ->

    val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    val request = Request.Builder()
            .url(serverUrl)
            .build()

    client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.d("WebSocket", "onOpen: response = $response")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            t.printStackTrace()
            Log.d("WebSocket", "onFailure: throwable = $t, response = $response")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d("WebSocket", "onClosing: code = $code, reason = $reason")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            if (text == "o") {
                Log.d("WebSocket", "Socket Opened: $text")
            } else if (text[0] == 'a') {
                val moshi = Moshi.Builder().build()

                val wsResponseListType = Types.newParameterizedType(
                        List::class.java,
                        String::class.java
                )
                val wsResponseListAdapter = moshi
                        .adapter<List<String>>(wsResponseListType)

                val wsResponseList = wsResponseListAdapter
                        .fromJson(text.substring(1))

                wsResponseList?.let { responseList ->
                    responseList.getOrNull(0)?.also { response ->
                        val wsResponseAdapter = moshi
                                .adapter<WebSocketResponse>(WebSocketResponse::class.java)

                        wsResponseAdapter.fromJson(response)
                                ?.let {
                                    onSentMessageCallbacks[it.requestId]
                                            ?.also { callback: (WebSocketResponse) -> Unit ->
                                                callback.invoke(it)
                                                onSentMessageCallbacks.remove(it.requestId)
                                            }
                                }
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d("WebSocket", "onClosed: code = $code, reason = $reason")
        }
    })
}) {
    val onSentMessageCallbacks = HashMap<Int, (WebSocketResponse) -> Unit>()
    var requestId = 0

    fun sendRequest(serverUrl: String, request: WebSocketRequest) {
        val moshi = Moshi.Builder()
                .add(WebSocketRequestCustomJsonAdapter())
                .build()

        // Convert Web Socket Request Object into a pure JSON String
        val wsRequestAdapter = moshi
                .adapter<WebSocketRequest>(WebSocketRequest::class.java)
                .serializeNulls()

        val wsRequest = wsRequestAdapter.toJson(request)

        // Add the above JSON String into a List
        val listRequest = ArrayList<String>()
        listRequest.add(wsRequest)

        // Converting the above list into a List of String JSON Request
        // Produced JSON Request: "[
        //      "{\"id\":0,\"method\":\"map.list\",\"msg\":\"method\",\"params\":{}}"
        // ]"
        val listStringType = Types.newParameterizedType(
                List::class.java,
                String::class.java
        )
        val wsListRequestAdapter = moshi
                .adapter<List<String>>(listStringType)

        // toJson => ["{\"id\":0,\"method\":\"map.list\",\"msg\":\"method\",\"params\":{}}"]
        val wsListRequest = wsListRequestAdapter.toJson(listRequest)

        WebSocketClient.getInstance(serverUrl).send(wsListRequest)
    }
}