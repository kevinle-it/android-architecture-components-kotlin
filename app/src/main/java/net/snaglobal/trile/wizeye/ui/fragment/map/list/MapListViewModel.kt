package net.snaglobal.trile.wizeye.ui.fragment.map.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.remote.RemoteContract
import net.snaglobal.trile.wizeye.data.remote.map.MapWebSocketContract
import net.snaglobal.trile.wizeye.data.remote.model.MapListItem
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest

/**
 * [ViewModel] for [MapListFragment] to retain data on configuration changes
 * and observe for [MapListFragment]'s lifecycle to remove data when
 * [MapListFragment] is destroyed completely.
 *
 * @author lmtri
 * @since Oct 28, 2018 at 6:41 PM
 */
class MapListViewModel(app: Application) : AndroidViewModel(app) {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    val currentMapList = ArrayList<MapListItem>()

    private val mapListNotifier = MediatorLiveData<List<MapListItem>>()

    fun getMapList(): LiveData<List<MapListItem>> {
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
                                        method = MapWebSocketContract.GET_MAP_LIST_METHOD
                                )
                                mapListNotifier.addSource(
                                        dataRepository.getMapList(serverUrl, wsResquest),
                                        mapListNotifier::postValue
                                )
                            }
                        }, {
                            it.printStackTrace()
                        })
        )
        return mapListNotifier
    }

    /**
     * Called when [MapListFragment] is destroyed completely.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}