package net.snaglobal.trile.wizeye.ui.fragment.about


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_about.*

import net.snaglobal.trile.wizeye.R

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since Nov 06, 2018 at 11:00 PM
 */
class AboutFragment : Fragment() {

    private val sharedAboutViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SharedAboutViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        action_back.setOnClickListener {
            findNavController().popBackStack()
        }

        if (sharedAboutViewModel.previousFragmentName.isNotEmpty()) {
            action_back_title.text = getString(
                    R.string.about_screen_action_back_to_title,
                    sharedAboutViewModel.previousFragmentName.toUpperCase()
            )
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment AboutFragment.
         */
        @JvmStatic
        fun newInstance() = AboutFragment()
    }
}
