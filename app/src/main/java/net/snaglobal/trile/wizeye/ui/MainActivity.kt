package net.snaglobal.trile.wizeye.ui

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity
import net.snaglobal.trile.wizeye.ui.fragment.MainFragment
import net.snaglobal.trile.wizeye.ui.fragment.about.AboutFragment
import net.snaglobal.trile.wizeye.ui.fragment.about.SharedAboutViewModel
import net.snaglobal.trile.wizeye.ui.fragment.video.detail.VideoDetailFragment
import net.snaglobal.trile.wizeye.utils.observeOnce

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel by lazy {
        ViewModelProviders
                .of(this)
                .get(MainActivityViewModel::class.java)
    }

    private val sharedAboutViewModel by lazy {
        ViewModelProviders
                .of(this)
                .get(SharedAboutViewModel::class.java)
    }

    private val actionMorePopupWindow by lazy {
        PopupWindow(this)
    }
    private val popupMenuLayout by lazy {
        LayoutInflater.from(this)
                .inflate(R.layout.toolbar_action_more_popup_menu, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel.isToolbarVisible.observe(this, Observer {
            when (it) {
                true -> main_toolbar.visibility = View.VISIBLE
                false -> main_toolbar.visibility = View.GONE
                else -> main_toolbar.visibility = View.GONE
            }
        })

        action_more.post {
            action_more.setOnClickListener {
                val location = IntArray(2)
                it.getLocationOnScreen(location)

                val actionMoreLocation = Point(location[0], location[1])

                actionMorePopupWindow.contentView = popupMenuLayout
                // Make the background of the Popup Window to be transparent before applying
                // rounded corners background in the [toolbar_action_more_popup_menu] layout
                // to avoid the effect of appearing small partly opaque squares at the corners
                actionMorePopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                actionMorePopupWindow.width = resources.getDimensionPixelSize(R.dimen.toolbar_action_more_popup_menu_width)
                actionMorePopupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT
                actionMorePopupWindow.isFocusable = true    // Tap outside the popup also dismiss it
                actionMorePopupWindow.showAtLocation(
                        it,
                        Gravity.NO_GRAVITY,
                        actionMoreLocation.x,
                        actionMoreLocation.y + action_more.height
                )
            }
        }

        // Setup Popup Menu Layout on App Opening
        mainActivityViewModel.getCurrentUserInfo().observe(this, Observer {
            it?.let { user ->
                setupPopupMenuLayout(user)
            }
        })
        // Setup Popup Menu Layout after a successful logging in
        mainActivityViewModel.loginSuccessfulNotifier.observe(this, Observer {
            mainActivityViewModel.getCurrentUserInfo().observeOnce(this, Observer { userEntity ->
                userEntity?.let { user ->
                    setupPopupMenuLayout(user)
                }
            })
        })
    }

    private fun setupPopupMenuLayout(user: LoginCredentialEntity) {
        popupMenuLayout.findViewById<TextView>(
                R.id.toolbar_action_more_popup_menu_username
        ).text = user.userId

        popupMenuLayout.findViewById<TextView>(
                R.id.toolbar_action_more_popup_menu_email
        ).text = user.domain

        popupMenuLayout.findViewById<LinearLayout>(
                R.id.toolbar_action_more_popup_menu_action_about
        ).setOnClickListener {
            actionMorePopupWindow.dismiss()

            val currentFragmnet = nav_host_fragment.childFragmentManager.fragments[0]

            when (currentFragmnet) {
                is MainFragment -> {
                    sharedAboutViewModel.previousFragmentName = getString(R.string.main_screen_name)
                    nav_host_fragment.findNavController()
                            .navigate(R.id.action_mainFragment_to_aboutFragment)
                }
                is VideoDetailFragment -> {
                    sharedAboutViewModel.previousFragmentName = getString(R.string.video_detail_screen_name)
                    nav_host_fragment.findNavController()
                            .navigate(R.id.action_videoDetailFragment_to_aboutFragment)
                }
            }
        }

        popupMenuLayout.findViewById<LinearLayout>(
                R.id.toolbar_action_more_popup_menu_action_logout
        ).setOnClickListener {
            actionMorePopupWindow.dismiss()

            val logoutDialogLayout = LayoutInflater.from(this)
                    .inflate(R.layout.custom_logout_confirmation_dialog, null)

            val logoutDialog = AlertDialog.Builder(this)
                    .setView(logoutDialogLayout)
                    .create()
            // Make the background of the Dialog Window to be transparent before applying
            // rounded corners background in the [custom_logout_confirmation_dialog] layout
            // to avoid the effect of appearing small white squares at the corners
            logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            logoutDialog.show()

            logoutDialogLayout.findViewById<TextView>(R.id.action_cancel).setOnClickListener {
                logoutDialog.dismiss()
            }
            logoutDialogLayout.findViewById<TextView>(R.id.action_logout).setOnClickListener {
                logoutDialog.dismiss()

                val currentFragmnet = nav_host_fragment.childFragmentManager.fragments[0]

                when (currentFragmnet) {
                    is MainFragment -> {
                        nav_host_fragment.findNavController()
                                .navigate(R.id.action_mainFragment_to_loginFragment)
                    }
                    is VideoDetailFragment -> {
                        nav_host_fragment.findNavController()
                                .navigate(R.id.action_videoDetailFragment_to_loginFragment)
                    }
                    is AboutFragment -> {
                        nav_host_fragment.findNavController()
                                .navigate(R.id.action_aboutFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}
