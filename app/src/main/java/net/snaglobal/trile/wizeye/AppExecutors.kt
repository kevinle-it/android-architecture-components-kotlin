package net.snaglobal.trile.wizeye

import android.os.Handler
import android.os.Looper
import net.snaglobal.trile.wizeye.utils.SingletonHolder

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation
 * (e.g. disk reads don't wait behind webservice requests).
 *
 * @author lmtri
 * @since Oct 11, 2018 at 8:20 AM
 */
open class AppExecutors private constructor(
        private val diskIO: Executor,
        private val networkIO: Executor,
        private val mainThread: Executor
) {

    companion object : SingletonHolder<AppExecutors, Unit>({
        AppExecutors(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3),
                MainThreadExecutor()
        )
    })

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}