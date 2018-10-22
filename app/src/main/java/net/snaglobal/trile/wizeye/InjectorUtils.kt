package net.snaglobal.trile.wizeye

import net.snaglobal.trile.wizeye.data.remote.RemoteDataSource
import net.snaglobal.trile.wizeye.data.repository.DataRepository

/**
 * Provides static methods to inject the various classes needed for this app.
 *
 * @author lmtri
 * @since Oct 18, 2018 at 2:51 PM
 */
object InjectorUtils {
    @JvmStatic
    fun provideAppExecutors() = AppExecutors.getInstance(Unit)

    @JvmStatic
    fun provideRemoteDataSource() = RemoteDataSource.getInstance(Unit)

    @JvmStatic
    fun provideRepository() = DataRepository.getInstance(
            provideRemoteDataSource(), provideAppExecutors()
    )
}