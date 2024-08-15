package com.cuongngo.core_project.services.network.invoker

import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.utils.Constants
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiClientFactory {
    companion object {
        inline fun <reified T> createService(networkConnectionInterceptor: NetworkConnectionInterceptor? = null): T {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val okkHttpClient = OkHttpClient.Builder().apply {
                sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                hostnameVerifier { _: String, _: SSLSession -> true }
            }.addInterceptor(BaseInterceptor().apply {
                setToken("Bearer ${AppPreferences.getUserAccessToken()}")
            }).apply {
                    connectTimeout(60, TimeUnit.SECONDS)
                    readTimeout(60, TimeUnit.SECONDS)
                    writeTimeout(60, TimeUnit.SECONDS)
                    connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                    protocols(listOf(Protocol.HTTP_1_1))
            }

            if (networkConnectionInterceptor != null) {
                okkHttpClient.addInterceptor(networkConnectionInterceptor)
            }

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okkHttpClient.addInterceptor(loggingInterceptor)

            return Retrofit.Builder()
                .client(okkHttpClient.build())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(T::class.java)
        }
    }
}