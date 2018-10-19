package net.snaglobal.trile.wizeye.api.login

import net.snaglobal.trile.wizeye.api.model.LoginCredential
import net.snaglobal.trile.wizeye.api.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author lmtri
 * @since Oct 12, 2018 at 10:11 AM
 */
interface ILoginClient {

    @POST("login")
    fun login(@Body loginCredential: LoginCredential): Call<LoginResponse?>
}