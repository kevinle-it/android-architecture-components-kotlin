package net.snaglobal.trile.wizeye.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.ui.MainActivityViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since Sep 25, 2018 at 11:26 AM
 */
class MainFragment : Fragment() {

    private val mainActivityViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
                .of(activity!!)
                .get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mainActivityViewModel.isToolbarVisible.value = true

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
