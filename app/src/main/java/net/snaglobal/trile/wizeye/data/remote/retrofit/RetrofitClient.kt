package net.snaglobal.trile.wizeye.data.remote.retrofit

import android.util.Log
import net.snaglobal.trile.wizeye.AppExecutors
import net.snaglobal.trile.wizeye.utils.SingletonHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author lmtri
 * @since Sep 28, 2018 at 8:37 AM
 */
object RetrofitClient : SingletonHolder<Retrofit, String>({ httpUrl: String ->
    val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        Log.d("API", it)
    })
    logger.level = HttpLoggingInterceptor.Level.BODY

    val httpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val original = chain.request()

                chain.proceed(original)
            }
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

    Retrofit.Builder()
            .baseUrl(if (httpUrl.endsWith('/')) httpUrl else "$httpUrl/")
            .client(httpClient)
            .callbackExecutor(AppExecutors.getInstance(Unit).networkIO())
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .build()
})