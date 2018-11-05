package net.snaglobal.trile.wizeye.ui.fragment.video.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.remote.RemoteContract
import net.snaglobal.trile.wizeye.data.remote.model.VideoDetail
import net.snaglobal.trile.wizeye.data.remote.video.VideoWebSocketContract
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketClient
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequest
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestParams
import net.snaglobal.trile.wizeye.data.remote.websocket.WebSocketRequestQuery

/**
 * [ViewModel] for [VideoDetailFragment] to retain data on configuration changes
 * and observe for [VideoDetailFragment]'s lifecycle to remove data when
 * [VideoDetailFragment] is destroyed completely.
 *
 * @author lmtri
 * @since Nov 02, 2018 at 10:14 AM
 */
class VideoDetailViewModel(app: Application) : AndroidViewModel(app) {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    private val videoDetailNotifier = MediatorLiveData<VideoDetail?>()

    fun getVideoDetail(cameraName: String): LiveData<VideoDetail?> {
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
                                                        name = cameraName
                                                )
                                        )
                                )
                                videoDetailNotifier.addSource(
                                        dataRepository.getVideoDetail(serverUrl, wsResquest),
                                        videoDetailNotifier::postValue
                                )
                            }
                        }, {
                            it.printStackTrace()
                        })
        )
        return videoDetailNotifier
    }

    /**
     * Called when [VideoDetailFragment] is destroyed completely.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}