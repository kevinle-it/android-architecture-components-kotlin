package net.snaglobal.trile.wizeye.ui.fragment.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.remote.model.LoginResponse

/**
 * [ViewModel] for [LoginFragment] to retain data on configuration changes
 * and observe for [LoginFragment]'s lifecycle to remove data when
 * [LoginFragment] is destroyed completely.
 *
 * @author lmtri
 * @since Oct 16, 2018 at 3:13 PM
 */
class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    private val loginSuccessfulNotifier by lazy {
        MutableLiveData<LoginResponse?>()
    }
    private val loginErrorNotifier by lazy {
        MutableLiveData<Throwable?>()
    }

    val loginSuccessful: LiveData<LoginResponse?>
        get() = loginSuccessfulNotifier

    val loginError: LiveData<Throwable?>
        get() = loginErrorNotifier

    fun login(domain: String, username: String, password: String) {
        compositeDisposable.add(
                dataRepository.login(domain, username, password)
                        .subscribe(
                                loginSuccessfulNotifier::postValue,
                                loginErrorNotifier::postValue
                        )
        )
    }

    /**
     * Called when [LoginFragment] is destroyed completely.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}