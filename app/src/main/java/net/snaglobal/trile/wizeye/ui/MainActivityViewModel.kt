package net.snaglobal.trile.wizeye.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import net.snaglobal.trile.wizeye.InjectorUtils
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity

/**
 * [ViewModel] for [MainActivity] to retain data on configuration changes
 * and observe for [MainActivity]'s lifecycle to remove data when
 * [MainActivity] is destroyed completely.
 *
 * @author lmtri
 * @since Sep 26, 2018 at 4:33 PM
 */
class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    val isToolbarVisible: MutableLiveData<Boolean> = MutableLiveData()

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val dataRepository by lazy {
        InjectorUtils.provideRepository(app.applicationContext)
    }

    private val currentUserInfoNotifier = MutableLiveData<LoginCredentialEntity?>()

    fun getCurrentUserInfo(): LiveData<LoginCredentialEntity?> {
        compositeDisposable.add(
                dataRepository.getLastLoggedInCredential()
                        .subscribe({ optionalResult ->
                            currentUserInfoNotifier.postValue(optionalResult.value)
                        }, {
                            it.printStackTrace()
                        })
        )
        return currentUserInfoNotifier
    }

    /**
     * Called when [MainActivity] is destroyed completely.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}