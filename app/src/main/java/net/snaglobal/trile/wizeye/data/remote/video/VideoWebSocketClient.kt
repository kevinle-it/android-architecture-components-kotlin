package net.snaglobal.trile.wizeye.data.remote.video

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import net.snaglobal.trile.wizeye.data.remote.model.VideoDetail
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest

/**
 * @author lmtri
 * @since Oct 31, 2018 at 7:02 AM
 */
object VideoWebSocketClient {

    private val videoListNotifier = MutableLiveData<List<VideoListItem>>()
    private val videoDetailNotifier = MutableLiveData<VideoDetail?>()

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

    @Suppress("UNCHECKED_CAST")
    fun getVideoDetail(serverUrl: String, request: WebSocketRequest): LiveData<VideoDetail?> {

        WebSocketClient.onSentMessageCallbacks[WebSocketClient.requestId] = { webSocketResponse ->
            val wsResponseBody = webSocketResponse.result.body as List<Map<String, Any>>

            for (item in wsResponseBody) {
                val name = item["Name"] as String?
                val description = item["Description"] as String?

                val cameraProperties = item["Properties"] as Map<String, List<String>>?
                cameraProperties?.let {
                    val rtspId = it["RtspID"]?.getOrNull(0)
                    val rtspPassword = it["RtspPassword"]?.getOrNull(0)
                    val rtspUrl = it["rtsp"]?.getOrNull(0)

                    if (name == null || description == null
                            || rtspId == null || rtspPassword == null
                            || rtspUrl == null) {
                        videoDetailNotifier.postValue(null)
                    } else {
                        videoDetailNotifier.postValue(
                                VideoDetail(
                                        name, description,
                                        rtspId, rtspPassword,
                                        rtspUrl
                                )
                        )
                    }
                }
            }
        }
        ++WebSocketClient.requestId

        WebSocketClient.sendRequest(serverUrl, request)

        return videoDetailNotifier
    }
}