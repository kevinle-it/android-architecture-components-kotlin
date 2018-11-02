package net.snaglobal.trile.wizeye.data.remote.video

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest

/**
 * @author lmtri
 * @since Oct 31, 2018 at 7:02 AM
 */
object VideoWebSocketClient {

    private val videoListNotifier = MutableLiveData<List<VideoListItem>>()

    @Suppress("UNCHECKED_CAST")
    fun getVideoList(serverUrl: String, request: WebSocketRequest): LiveData<List<VideoListItem>> {

        WebSocketClient.onSentMessageCallbacks[WebSocketClient.requestId] = { webSocketResponse ->
            val wsResponseBody = webSocketResponse.result.body as List<Map<String, String>>

            val videoList = ArrayList<VideoListItem>()
            for (item in wsResponseBody) {
                item["Name"]?.also {
                    videoList.add(VideoListItem(it))
                }
            }

            videoListNotifier.postValue(videoList)
        }
        ++WebSocketClient.requestId

        WebSocketClient.sendRequest(serverUrl, request)

        return videoListNotifier
    }
}