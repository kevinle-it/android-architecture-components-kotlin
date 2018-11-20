package net.snaglobal.trile.wizeye.ui.fragment.about

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel

/**
 * Shared [ViewModel] for [AboutFragment] and other [Fragment]s to retain data on
 * configuration changes, save the name of the previous fragment which navigate to this
 * [AboutFragment], and observe for [MainActivity]'s lifecycle to remove data when [MainActivity]
 * is destroyed completely.
 *
 * @author lmtri
 * @since Nov 07, 2018 at 10:17 AM
 */
class SharedAboutViewModel(app: Application) : AndroidViewModel(app) {
    var previousFragmentName = ""
}