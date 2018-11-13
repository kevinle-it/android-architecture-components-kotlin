package net.snaglobal.trile.wizeye.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_main.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.ui.MainActivityViewModel
import net.snaglobal.trile.wizeye.ui.fragment.map.list.MapListFragment
import net.snaglobal.trile.wizeye.ui.fragment.video.list.VideoListFragment

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

    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(childFragmentManager)
    }

    private var prevMenuItem: MenuItem? = null

    private val viewPagerOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            prevMenuItem?.let {
                it.isChecked = false
            } ?: run {
                bottom_navigation.menu.getItem(0).isChecked = false
            }
            bottom_navigation.menu.getItem(position).isChecked = true
            prevMenuItem = bottom_navigation.menu.getItem(position)
        }
    }

    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    MainFragmentContract.NAVIGATION_INDEX_MAP -> {
                        if (view_pager.currentItem == MainFragmentContract.VIEW_PAGER_INDEX_MAP) {
                            return@OnNavigationItemSelectedListener false
                        }
                        view_pager.setCurrentItem(MainFragmentContract.VIEW_PAGER_INDEX_MAP, false)
                    }
                    MainFragmentContract.NAVIGATION_INDEX_ALERT -> {
                        if (view_pager.currentItem == MainFragmentContract.VIEW_PAGER_INDEX_ALERT) {
                            return@OnNavigationItemSelectedListener false
                        }
                        view_pager.setCurrentItem(MainFragmentContract.VIEW_PAGER_INDEX_ALERT, false)
                    }
                    MainFragmentContract.NAVIGATION_INDEX_CHART -> {
                        if (view_pager.currentItem == MainFragmentContract.VIEW_PAGER_INDEX_CHART) {
                            return@OnNavigationItemSelectedListener false
                        }
                        view_pager.setCurrentItem(MainFragmentContract.VIEW_PAGER_INDEX_CHART, false)
                    }
                    MainFragmentContract.NAVIGATION_INDEX_VIDEO -> {
                        if (view_pager.currentItem == MainFragmentContract.VIEW_PAGER_INDEX_VIDEO) {
                            return@OnNavigationItemSelectedListener false
                        }
                        view_pager.setCurrentItem(MainFragmentContract.VIEW_PAGER_INDEX_VIDEO, false)
                    }
                }
                true
            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mainActivityViewModel.isToolbarVisible.value = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.isPagingEnabled = false
        view_pager.offscreenPageLimit = MainFragmentContract.VIEW_PAGER_OFF_SCREEN_PAGE_LIMIT

        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = bottom_navigation.getChildAt(0) as ViewGroup
        for (index in 0 until menuView.childCount) {
            val item = menuView.getChildAt(index)

            val inactiveLabel = item?.findViewById<View>(R.id.smallLabel)
            inactiveLabel?.let {
                if (it is TextView) {
                    it.ellipsize = TextUtils.TruncateAt.END
                }
            }

            val activeLabel = item?.findViewById<View>(R.id.largeLabel)
            activeLabel?.let {
                if (it is TextView) {
                    it.setPadding(0, 0, 0, 0)
                    it.ellipsize = TextUtils.TruncateAt.END
                }
            }
        }

        setupViewPager()
    }

    override fun onStart() {
        super.onStart()
        view_pager.addOnPageChangeListener(viewPagerOnPageChangeListener)
    }

    override fun onStop() {
        view_pager.removeOnPageChangeListener(viewPagerOnPageChangeListener)
        super.onStop()
    }

    private fun setupViewPager() {
        viewPagerAdapter.addFragment(MapListFragment.newInstance())
        viewPagerAdapter.addFragment(MapListFragment.newInstance())
        viewPagerAdapter.addFragment(VideoListFragment.newInstance())
        viewPagerAdapter.addFragment(VideoListFragment.newInstance())

        view_pager.adapter = viewPagerAdapter
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
