package net.snaglobal.trile.wizeye.ui.fragment.video.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_video_detail.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.VideoDetail
import net.snaglobal.trile.wizeye.ui.fragment.video.SharedVideoListDetailViewModel
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

/**
 * A simple [Fragment] subclass.
 * Use the [VideoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author lmtri
 * @since Sep 26, 2018 at 10:59 PM
 */
class VideoDetailFragment : Fragment() {

    private val TAG = "VLC_MEDIA_PLAYER"

    private val sharedVideoListDetailViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this).get(SharedVideoListDetailViewModel::class.java)
        }
    }

    private val videoDetailViewModel by lazy {
        ViewModelProviders.of(this).get(VideoDetailViewModel::class.java)
    }
    private var currentVideoDetail: VideoDetail? = null

    private val libVLC by lazy {
        activity?.run {
            LibVLC(this)
        }
    }
    private val mediaPlayer by lazy {
        libVLC?.let {
            MediaPlayer(it)
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedVideoListDetailViewModel?.run {
            videoDetailViewModel.getVideoDetail(currentVideoItem.name).observe(
                    this@VideoDetailFragment,
                    Observer {
                        it?.let { videoDetail ->
                            camera_name.text = videoDetail.name
                            camera_description.text = videoDetail.description

                            camera_view.post {
                                streamVideoFrom(
                                        videoDetail.rtspUrl,
                                        videoDetail.rtspId, videoDetail.rtspPassword,
                                        camera_view.width, camera_view.height
                                )
                            }
                            currentVideoDetail = videoDetail
                        } ?: kotlin.run {
                            circular_progress_view.visibility = View.GONE
                            video_loading_error_message.visibility = View.VISIBLE
                        }
                    }
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedVideoListDetailViewModel == null) {
            camera_name.text = getString(R.string.video_detail_screen_empty_name_description)
            camera_description.text = getString(R.string.video_detail_screen_empty_name_description)

            circular_progress_view.visibility = View.GONE
            video_loading_error_message.visibility = View.VISIBLE

            Toast.makeText(
                    activity,
                    "Invalid Activity. Cannot Load Camera Data.",
                    Toast.LENGTH_SHORT
            ).show()
        }

        videoDetailViewModel.currentVideoDetail?.apply {
            camera_name.text = name
            camera_description.text = description

            camera_view.post {
                streamVideoFrom(
                        rtspUrl,
                        rtspId, rtspPassword,
                        camera_view.width, camera_view.height
                )
            }
        }

        action_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {

        // Important -> else, crashed because Surface View has NOT been detached
        releasePlayer().subscribe({
            // Do Nothing
        }, {
            it.printStackTrace()
        })

        super.onDestroyView()
    }

    override fun onDetach() {
        compositeDisposable.dispose()
        super.onDetach()
    }

    private fun streamVideoFrom(rtspUrl: String,
                                rtspId: String, rtspPassword: String,
                                videoWidth: Int, videoHeight: Int) {
        compositeDisposable.add(
                releasePlayer().andThen {
                    camera_view.holder.setKeepScreenOn(true)

                    mediaPlayer?.apply {
                        setEventListener(mediaPlayerEventListener)

                        vlcVout.setVideoView(camera_view)
                        vlcVout.setWindowSize(videoWidth, videoHeight)
                        vlcVout.attachViews()

                        val media = Media(libVLC, Uri.parse(
                                rtspUrl.replace(
                                        "//",
                                        "//$rtspId:$rtspPassword@"
                                )
                        ))
                        this.media = media

                        play()
                    }
                }.subscribeOn(Schedulers.computation()).subscribe({
                    // Do Nothing
                }, {
                    circular_progress_view.visibility = View.GONE
                    video_loading_error_message.visibility = View.VISIBLE

                    it.printStackTrace()
                })
        )
    }

    private fun releasePlayer() = Completable.fromAction {
        mediaPlayer?.stop()
        mediaPlayer?.vlcVout?.detachViews()
        libVLC?.release()
    }.subscribeOn(Schedulers.computation())

    private val mediaPlayerEventListener = MediaPlayer.EventListener { event ->
        event?.apply {
            when (type) {
                MediaPlayer.Event.MediaChanged -> Log.d(TAG, "MediaChanged")
                MediaPlayer.Event.Opening -> Log.d(TAG, "Opening")
                MediaPlayer.Event.Buffering -> {
                    Log.d(TAG, "Buffering")

                    if (circular_progress_view?.visibility != View.VISIBLE) {
                        circular_progress_view?.visibility = View.VISIBLE
                    }
                }
                MediaPlayer.Event.Playing -> {
                    Log.d(TAG, "Playing")

                    // This [currentVideoDetail] will be used again in [onViewCreated] when
                    // returning back to this [VideoDetailFragment] from [AboutFragment] or
                    // other [Fragment]s, so we will need to store it into [VideoDetailViewModel]
                    //
                    // Reason: When navigating to [AboutFragment], only [onDestroyView] get called,
                    // when returning back to this [VideoDetailFragment], only stages from
                    // [onCreateView] and after get called
                    // -> [onCreate] will NOT be called again
                    videoDetailViewModel.currentVideoDetail = currentVideoDetail
                }
                MediaPlayer.Event.PositionChanged -> Log.d(TAG, "PositionChanged")
                MediaPlayer.Event.TimeChanged -> {
                    Log.d(TAG, "TimeChanged")

                    if (circular_progress_view?.visibility != View.GONE) {
                        circular_progress_view?.visibility = View.GONE
                    }
                }
                MediaPlayer.Event.EncounteredError -> {
                    Log.d(TAG, "EncounteredError")

                    circular_progress_view?.visibility = View.GONE
                    video_loading_error_message?.visibility = View.VISIBLE

                    releasePlayer().subscribe({
                        // Do Nothing
                    }, {
                        it.printStackTrace()
                    })
                }
                MediaPlayer.Event.Stopped -> {
                    Log.d(TAG, "Stopped")

                    circular_progress_view?.visibility = View.GONE
                    video_loading_error_message?.visibility = View.VISIBLE

                    releasePlayer().subscribe({
                        // Do Nothing
                    }, {
                        it.printStackTrace()
                    })
                }
                else -> Log.d(TAG, "Other Events")
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment VideoDetailFragment.
         */
        @JvmStatic
        fun newInstance() = VideoDetailFragment()
    }
}
