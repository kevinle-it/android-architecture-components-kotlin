package net.snaglobal.trile.wizeye.api.login

import android.util.Log
import net.snaglobal.trile.wizeye.api.RetrofitClient
import net.snaglobal.trile.wizeye.api.model.LoginCredential
import net.snaglobal.trile.wizeye.api.model.LoginResponse
import java.io.IOException

/**
 * @author lmtri
 * @since Oct 15, 2018 at 3:00 PM
 */
object LoginClient {
    @JvmStatic
    @Throws(IOException::class)
    fun login(httpUrl: String, loginCredential: LoginCredential): LoginResponse? {
        val loginClient = RetrofitClient.getInstance(httpUrl)
                .create(ILoginClient::class.java)

        val call = loginClient.login(loginCredential)

        val response = call.execute()

        if (response.isSuccessful) {
            response.body()?.run {
                Log.d("API", "successfulStatus: $successfulStatus")
                Log.d("API", "message: $message")
                Log.d("API", "token: $token")
                return this
            }
        }
        return null
    }
}