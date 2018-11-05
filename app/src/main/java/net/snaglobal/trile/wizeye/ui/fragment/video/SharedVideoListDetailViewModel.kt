package net.snaglobal.trile.wizeye.ui.fragment.video

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem

/**
 * Shared [ViewModel] for [VideoListFragment] and [VideoDetailFragment] to retain data on
 * configuration changes, communicate between [VideoListFragment] and [VideoDetailFragment],
 * and observe for [MainActivity]'s lifecycle to remove data when [MainActivity] is destroyed
 * completely.
 *
 * @author lmtri
 * @since Nov 02, 2018 at 10:14 AM
 */
class SharedVideoListDetailViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var currentVideoItem: VideoListItem
}