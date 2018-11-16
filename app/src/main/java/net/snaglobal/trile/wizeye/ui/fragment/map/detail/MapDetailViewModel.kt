package net.snaglobal.trile.wizeye.ui.fragment.map.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.remote.RemoteContract
import net.snaglobal.trile.wizeye.data.remote.map.MapWebSocketContract
import net.snaglobal.trile.wizeye.data.remote.model.MapDetail
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest

/**
 * [ViewModel] for [MapDetailFragment] to retain data on configuration changes
 * and observe for [MapDetailFragment]'s lifecycle to remove data when
 * [MapDetailFragment] is destroyed completely.
 *
 * @author lmtri
 * @since Nov 13, 2018 at 4:45 PM
 */
class MapDetailViewModel(app: Application) : AndroidViewModel(app) {

    var currentMapViewInfo: Pair<String, MapDetail>? = null

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    private val mapDetailNotifier = MediatorLiveData<Pair<String, MapDetail?>>()

    fun getMapDetail(): LiveData<Pair<String, MapDetail?>> {
        compositeDisposable.add(
                dataRepository.getLastLoggedInCredential()
                        .subscribe({ optionalResult ->
                            optionalResult.value?.let { lastLoggedInCredential ->
                                val serverUrl = RemoteContract.WEB_SOCKET_URL
                                        .format(
                                                lastLoggedInCredential.domain,
                                                lastLoggedInCredential.token
                                        )
                                val wsResquest = WebSocketRequest(
                                        requestId = WebSocketClient.requestId,
                                        method = MapWebSocketContract.OPEN_MAP_METHOD
                                )
                                mapDetailNotifier.addSource(
                                        dataRepository.getMapDetail(serverUrl, wsResquest)
                                ) {
                                    mapDetailNotifier.postValue(
                                            Pair(lastLoggedInCredential.domain, it)
                                    )
                                }
                            }
                        }, {
                            it.printStackTrace()
                        })
        )
        return mapDetailNotifier
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}