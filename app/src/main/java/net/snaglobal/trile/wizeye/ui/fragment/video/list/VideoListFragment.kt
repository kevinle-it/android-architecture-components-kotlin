package net.snaglobal.trile.wizeye.ui.fragment.video.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_video_list.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem
import net.snaglobal.trile.wizeye.ui.fragment.video.SharedVideoListDetailViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [VideoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since Oct 31, 2018 at 7:00 AM
 */
class VideoListFragment : Fragment() {

    private val videoListViewModel by lazy {
        ViewModelProviders.of(this).get(VideoListViewModel::class.java)
    }

    private val sharedVideoListDetailViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this).get(SharedVideoListDetailViewModel::class.java)
        }
    }

    private val videoItemAdapter = VideoItemAdapter(object : VideoItemAdapter.OnVideoItemClickListener {
        override fun onClick(videoItem: VideoListItem) {
            sharedVideoListDetailViewModel?.run {
                currentVideoItem = videoItem
                findNavController().navigate(R.id.action_mainFragment_to_videoDetailFragment)
            } ?: activity?.run {
                Toast.makeText(
                        this,
                        "Invalid Activity. Cannot Navigate to Video Detail Screen",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    })

    /**
     * This [onCreate] stage will only be called once when this [VideoListFragment] is first created
     * and will NOT be called again when using [findNavController] to navigate back from
     * [VideoDetailFragment].
     *
     * The reason is the last stage will be called when using [findNavController] to navigate from
     * this [VideoListFragment] to [VideoDetailFragment] is [onDestroyView].
     *
     * @see <a href="https://github.com/xxv/android-lifecycle">
     *          The Complete Android Activity/Fragment Lifecycle
     *     </a>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        videoListViewModel.getVideoList().observe(this, Observer {
            it?.let { videoList ->
                videoListViewModel.currentVideoList.clear()
                videoListViewModel.currentVideoList.addAll(videoList)

                videoItemAdapter.submitList(videoList)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        video_list.adapter = videoItemAdapter

        if (videoListViewModel.currentVideoList.isNotEmpty()) {
            videoItemAdapter.submitList(videoListViewModel.currentVideoList)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment VideoListFragment.
         */
        @JvmStatic
        fun newInstance() = VideoListFragment()
    }
}
