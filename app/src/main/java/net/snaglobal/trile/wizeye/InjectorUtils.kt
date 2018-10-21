package net.snaglobal.trile.wizeye

import android.content.Context
import net.snaglobal.trile.wizeye.data.remote.RemoteDataSource
import net.snaglobal.trile.wizeye.data.repository.DataRepository
import net.snaglobal.trile.wizeye.data.room.RoomDataSource

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
    fun provideRoomDataSource(context: Context) = RoomDataSource.getInstance(context)

    @JvmStatic
    fun provideRepository(context: Context) = DataRepository.getInstance(
            provideRoomDataSource(context), provideRemoteDataSource(), provideAppExecutors()
    )
}