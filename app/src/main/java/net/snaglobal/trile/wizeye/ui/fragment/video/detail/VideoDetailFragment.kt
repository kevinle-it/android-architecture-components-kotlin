package net.snaglobal.trile.wizeye.ui.fragment.video.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_video_detail.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.ui.fragment.video.SharedVideoListDetailViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [VideoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author lmtri
 * @since Sep 26, 2018 at 10:59 PM
 */
class VideoDetailFragment : Fragment() {

    private val sharedVideoListDetailViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this).get(SharedVideoListDetailViewModel::class.java)
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
            // TODO: Sep-26-2018 Perform Streaming Camera Video
        } ?: kotlin.run {
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
