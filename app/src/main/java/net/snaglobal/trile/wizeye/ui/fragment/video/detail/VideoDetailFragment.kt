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
import kotlinx.android.synthetic.main.fragment_video_detail.*
import net.snaglobal.trile.wizeye.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        } ?: kotlin.run {
                            circular_progress_view.visibility = View.GONE
                            video_loading_error_message.visibility = View.VISIBLE
                        }
                    }
            )
        } ?: kotlin.run {
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

        action_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {

        releasePlayer() // Important -> else, crashed because Surface View has NOT been detached

        super.onDestroyView()
    }

    private fun streamVideoFrom(rtspUrl: String,
                                rtspId: String, rtspPassword: String,
                                videoWidth: Int, videoHeight: Int) {
        try {
            releasePlayer()

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
        } catch (e: Exception) {
            circular_progress_view.visibility = View.GONE
            video_loading_error_message.visibility = View.VISIBLE

            e.printStackTrace()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.vlcVout?.detachViews()
        libVLC?.release()
    }

    private val mediaPlayerEventListener = MediaPlayer.EventListener { event ->
        event?.apply {
            when (type) {
                MediaPlayer.Event.MediaChanged -> Log.d(TAG, "MediaChanged")
                MediaPlayer.Event.Opening -> Log.d(TAG, "Opening")
                MediaPlayer.Event.Buffering -> {
                    Log.d(TAG, "Buffering")

                    if (circular_progress_view.visibility != View.VISIBLE) {
                        circular_progress_view.visibility = View.VISIBLE
                    }
                }
                MediaPlayer.Event.Playing -> Log.d(TAG, "Playing")
                MediaPlayer.Event.PositionChanged -> Log.d(TAG, "PositionChanged")
                MediaPlayer.Event.TimeChanged -> {
                    Log.d(TAG, "TimeChanged")

                    if (circular_progress_view.visibility != View.GONE) {
                        circular_progress_view.visibility = View.GONE
                    }
                }
                MediaPlayer.Event.EncounteredError -> {
                    Log.d(TAG, "EncounteredError")

                    circular_progress_view.visibility = View.GONE
                    video_loading_error_message.visibility = View.VISIBLE

                    releasePlayer()
                }
                MediaPlayer.Event.Stopped -> {
                    Log.d(TAG, "Stopped")

                    circular_progress_view.visibility = View.GONE
                    video_loading_error_message.visibility = View.VISIBLE

                    releasePlayer()
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
