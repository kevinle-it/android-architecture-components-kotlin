package net.snaglobal.trile.wizeye.ui.fragment

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.ui.MainActivityViewModel

/**
 * @author trile
 * @since Sep 24, 2018 at 10:51 AM
 *
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {
    private val SPLASH_DELAY: Long = 3000

    private val mainActivityViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(activity!!.applicationContext)
    }

    private val loggedInNotifier by lazy {
        MutableLiveData<Boolean?>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        mainActivityViewModel.isToolbarVisible.value = false

        loggedInNotifier.observe(this, Observer { isLoggedIn: Boolean? ->
            Handler().postDelayed({
                isLoggedIn?.let {
                    if (it) {
                        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }, SPLASH_DELAY)
        })
        checkIfLoggedInOnAppOpen()

        return view
    }

    private fun checkIfLoggedInOnAppOpen() =
            compositeDisposable.add(
                    dataRepository.checkIfLoggedInOnAppOpen()
                            .subscribe({
                                loggedInNotifier.postValue(it)
                            }, {
                                it.printStackTrace()
                            })
            )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment SplashFragment.
         */
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}
