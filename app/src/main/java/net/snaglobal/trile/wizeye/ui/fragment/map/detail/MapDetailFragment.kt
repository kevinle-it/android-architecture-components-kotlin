package net.snaglobal.trile.wizeye.ui.fragment.map.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_map_detail.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.RemoteContract

/**
 * A simple [Fragment] subclass.
 * Use the [MapDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since Nov 13, 2018 at 4:42 PM
 */
class MapDetailFragment : Fragment() {

    private val mapDetailViewModel by lazy {
        ViewModelProviders.of(this).get(MapDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapDetailViewModel.getMapDetail().observe(this, Observer { data ->
            data?.let {
                it.second?.apply {
                    val httpUrl = RemoteContract.GET_MAP_VIEW_URL.format(it.first, token, mapId)

                    mapDetailViewModel.currentMapViewInfo = Pair(it.first, this)

                    map_view.settings.javaScriptEnabled = true
                    map_view.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            // The page only actually shows in the second time
                            // this [onPageFinished] get called
                            mapDetailViewModel.apply {
                                if (isAllowedToDismissCircularProgress == true) {
                                    isAllowedToDismissCircularProgress = false

                                    map_view.visibility = View.VISIBLE
                                    circular_progress_view.visibility = View.GONE
                                } else {
                                    isAllowedToDismissCircularProgress = true
                                }
                            }
                        }
                    }
                    map_view.loadUrl(httpUrl)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapDetailViewModel.currentMapViewInfo?.let {
            it.second.apply {
                val httpUrl = RemoteContract.GET_MAP_VIEW_URL.format(it.first, token, mapId)

                map_view.settings.javaScriptEnabled = true
                map_view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // The page only actually shows in the second time
                        // this [onPageFinished] get called
                        mapDetailViewModel.apply {
                            if (isAllowedToDismissCircularProgress == true) {
                                isAllowedToDismissCircularProgress = false

                                map_view.visibility = View.VISIBLE
                                circular_progress_view.visibility = View.GONE
                            } else {
                                isAllowedToDismissCircularProgress = true
                            }
                        }
                    }
                }
                map_view.loadUrl(httpUrl)
            }
        }
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
