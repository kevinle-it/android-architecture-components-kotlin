package net.snaglobal.trile.wizeye

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel by lazy {
        ViewModelProviders
                .of(this)
                .get(MainActivityViewModel::class.java)
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
    }
}
