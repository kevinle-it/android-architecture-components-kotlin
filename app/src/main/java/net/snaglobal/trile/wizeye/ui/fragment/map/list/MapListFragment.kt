package net.snaglobal.trile.wizeye.ui.fragment.map.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_map_list.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.MapListItem

/**
 * @author trile
 * @since Sep 26, 2018 at 3:28 PM
 *
 * A simple [Fragment] subclass.
 * Use the [MapListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapListFragment : Fragment() {

    private val mapListViewModel by lazy {
        ViewModelProviders.of(this).get(MapListViewModel::class.java)
    }

    private val mapItemAdapter = MapItemAdapter(object : MapItemAdapter.OnMapItemClickListener {
        override fun onClick(mapItem: MapListItem) {
            findNavController().navigate(R.id.action_mainFragment_to_mapDetailFragment)
        }
    })

    /**
     * This [onCreate] stage will only be called once when this [MapListFragment] is first created
     * and will NOT be called again when using [findNavController] to navigate back from
     * [MapDetailFragment].
     *
     * The reason is the last stage will be called when using [findNavController] to navigate from
     * this [MapListFragment] to [MapDetailFragment] is [onDestroyView].
     *
     * @see <a href="https://github.com/xxv/android-lifecycle">
     *          The Complete Android Activity/Fragment Lifecycle
     *     </a>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapListViewModel.getMapList().observe(this, Observer {
            it?.let { mapList ->
                mapListViewModel.currentMapList.clear()
                mapListViewModel.currentMapList.addAll(mapList)

                mapItemAdapter.submitList(mapList)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map_list.adapter = mapItemAdapter

        if (mapListViewModel.currentMapList.isNotEmpty()) {
            mapItemAdapter.submitList(mapListViewModel.currentMapList)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment MapListFragment.
         */
        @JvmStatic
        fun newInstance() = MapListFragment()
    }
}
