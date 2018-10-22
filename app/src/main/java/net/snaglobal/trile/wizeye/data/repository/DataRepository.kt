package net.snaglobal.trile.wizeye.data.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.snaglobal.trile.wizeye.AppExecutors
import net.snaglobal.trile.wizeye.data.remote.RemoteDataSource
import net.snaglobal.trile.wizeye.data.remote.model.LoginResponse
import net.snaglobal.trile.wizeye.data.room.RoomDataSource
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity

/**
 * Single Data Source of Truth of the whole Application.
 *
 * @author lmtri
 * @since Oct 16, 2018 at 3:35 PM
 */
class DataRepository(
        private val roomDataSource: RoomDataSource,
        private val remoteDataSource: RemoteDataSource,
        private val executors: AppExecutors
) {

    fun login(domain: String, username: String, password: String): Single<LoginResponse?> =
            Single.defer {
                Single.just(
                        remoteDataSource.login(domain, username, password)
                )
            }.flatMap {
                roomDataSource.loginCredentialDao()
                        .insertLoginCredential(
                                LoginCredentialEntity(domain, username, password, it.token)
                        )
                return@flatMap Single.just(it)
            }.subscribeOn(
                    Schedulers.from(executors.networkIO())
            ).observeOn(
                    AndroidSchedulers.mainThread()
            )


    companion object {
        @Volatile private var instance: DataRepository? = null

        fun getInstance(
                roomDataSource: RoomDataSource,
                remoteDataSource: RemoteDataSource,
                executors: AppExecutors
        ): DataRepository =
                instance ?: synchronized(this) {
                    instance ?: DataRepository(roomDataSource, remoteDataSource, executors).also {
                        instance = it
                    }
                }
    }
}