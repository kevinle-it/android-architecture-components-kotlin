package net.snaglobal.trile.wizeye.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * [ViewModel] for [MainActivity] to retain data on configuration changes
 * and observe for [MainActivity]'s lifecycle to remove data when
 * [MainActivity] is destroyed completely.
 *
 * @author lmtri
 * @since Sep 26, 2018 at 4:33 PM
 */
class MainActivityViewModel : ViewModel() {
    val isToolbarVisible: MutableLiveData<Boolean> = MutableLiveData()
}