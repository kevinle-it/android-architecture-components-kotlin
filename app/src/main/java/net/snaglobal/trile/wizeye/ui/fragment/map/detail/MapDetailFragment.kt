package net.snaglobal.trile.wizeye.ui.fragment.map.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.snaglobal.trile.wizeye.R

/**
 * A simple [Fragment] subclass.
 * Use the [MapDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since Nov 13, 2018 at 4:42 PM
 */
class MapDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_detail, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment MapDetailFragment.
         */
        @JvmStatic
        fun newInstance() = MapDetailFragment()
    }
}
