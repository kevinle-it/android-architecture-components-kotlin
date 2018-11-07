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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity
import net.snaglobal.trile.wizeye.ui.fragment.login.LoginViewModel
import net.snaglobal.trile.wizeye.utils.observeOnce

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel by lazy {
        ViewModelProviders
                .of(this)
                .get(MainActivityViewModel::class.java)
    }
    private val loginViewModel by lazy {
        ViewModelProviders
                .of(this)
                .get(LoginViewModel::class.java)
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
        loginViewModel.loginSuccessful.observeOnce(this, Observer {
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

            Toast.makeText(
                    this, "Action About Clicked!", Toast.LENGTH_SHORT
            ).show()
            // TODO: Nov-01-2018 Open About Fragment
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

                Toast.makeText(
                        this, "Log Out Clicked!", Toast.LENGTH_SHORT
                ).show()
                // TODO: Nov-06-2018 Perform Logging Out of Current User
            }
        }
    }
}
