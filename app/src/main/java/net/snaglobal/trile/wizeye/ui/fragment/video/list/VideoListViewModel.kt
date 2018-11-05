package net.snaglobal.trile.wizeye.ui.fragment.video.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.remote.RemoteContract
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem
import net.snaglobal.trile.wizeye.data.remote.video.VideoWebSocketContract
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestParams
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestQuery

/**
 * [ViewModel] for [VideoListFragment] to retain data on configuration changes
 * and observe for [VideoListFragment]'s lifecycle to remove data when
 * [VideoListFragment] is destroyed completely.
 *
 * @author lmtri
 * @since Oct 31, 2018 at 6:55 AM
 */
class VideoListViewModel(app: Application) : AndroidViewModel(app) {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    val currentVideoList = ArrayList<VideoListItem>()

    private val videoListNotifier = MediatorLiveData<List<VideoListItem>>()

    fun getVideoList(): LiveData<List<VideoListItem>> {
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
                                        method = VideoWebSocketContract.GET_VIDEO_METHOD,
                                        params = WebSocketRequestParams(
                                                query = WebSocketRequestQuery(
                                                        playerTypeName = VideoWebSocketContract
                                                                .GET_VIDEO_LIST_QUERY_PARAMS
                                                )
                                        )
                                )
                                videoListNotifier.addSource(
                                        dataRepository.getVideoList(serverUrl, wsResquest),
                                        videoListNotifier::postValue
                                )
                            }
                        }, {
                            it.printStackTrace()
                        })
        )
        return videoListNotifier
    }

    /**
     * Called when [VideoListFragment] is destroyed completely.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}