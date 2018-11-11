package net.snaglobal.trile.wizeye.data.remote.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import net.snaglobal.trile.wizeye.data.remote.model.MapListItem
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest

/**
 * @author lmtri
 * @since Oct 26, 2018 at 9:37 AM
 */
class MapWebSocketClient {

    private val mapListNotifier = MutableLiveData<List<MapListItem>>()

    @Suppress("UNCHECKED_CAST")
    fun getMapList(serverUrl: String, request: WebSocketRequest): LiveData<List<MapListItem>> {

        WebSocketClient.onSentMessageCallbacks[WebSocketClient.requestId] = { webSocketResponse ->
            val wsResponseBody = webSocketResponse.result.body as List<Map<String, String>>

            val mapList = ArrayList<MapListItem>()
            for (item in wsResponseBody) {
                item["DisplayName"]?.also {
                    mapList.add(MapListItem(it))
                }
            }

            mapListNotifier.postValue(mapList)
        }
        ++WebSocketClient.requestId

        WebSocketClient.sendRequest(serverUrl, request)

        return mapListNotifier
    }
}