package net.snaglobal.trile.wizeye.data.remote

import net.snaglobal.trile.wizeye.data.remote.login.LoginClient
import net.snaglobal.trile.wizeye.data.remote.map.MapWebSocketClient
import net.snaglobal.trile.wizeye.data.remote.model.LoginResponse
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest
import net.snaglobal.trile.wizeye.utils.SingletonHolder
import java.io.IOException

/**
 * Provides an API for handling all network operations.
 *
 * @author lmtri
 * @since Oct 17, 2018 at 4:49 PM
 */
class RemoteDataSource {

    @Throws(IOException::class)
    fun login(domain: String, username: String, password: String): LoginResponse? =
            LoginClient.login(domain, username, password)

    fun getMapList(serverUrl: String, request: WebSocketRequest) =
            MapWebSocketClient.getMapList(serverUrl, request)

    companion object : SingletonHolder<RemoteDataSource, Unit>({ RemoteDataSource() })
}