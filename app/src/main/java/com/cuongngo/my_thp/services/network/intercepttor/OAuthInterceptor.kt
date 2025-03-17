package com.cuongngo.my_thp.services.network.intercepttor

import com.cuongngo.my_thp.data.local.AppPreferences
import okhttp3.Interceptor
class OAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
//        val userId = AppPreferences.getUserID()
//        val platform = PLATFORM_ANDROID
//        val appVersion = BuildConfig.VERSION_NAME
        val bearerToken = "Bearer ${AppPreferences.getUserAccessToken()}"

        request = request.newBuilder()
            .header("Authorization", bearerToken)
//            .header("Platform", platform)
//            .header("version", appVersion)
//            .header("LocationID", "$locationId")
//            .header("UserID", userId)
            .build()
        return chain.proceed(request)
    }
}