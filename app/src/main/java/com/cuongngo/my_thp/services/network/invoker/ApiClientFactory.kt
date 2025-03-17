package com.cuongngo.my_thp.services.network.invoker

import com.cuongngo.my_thp.utils.Constants
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiClientFactory {
    var retrofit: Retrofit? = null
    val baseInterceptor = BaseInterceptor()

    inline fun <reified T> createService(networkConnectionInterceptor: NetworkConnectionInterceptor? = null): T {
        if (retrofit == null) {
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

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
                    arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val okHttpClientBuilder = OkHttpClient.Builder().apply {
                sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                hostnameVerifier { _, _ -> true }
                addInterceptor(baseInterceptor)
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                protocols(listOf(Protocol.HTTP_1_1))
            }

            networkConnectionInterceptor?.let {
                okHttpClientBuilder.addInterceptor(it)
            }

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(loggingInterceptor)

            val okHttpClient = okHttpClientBuilder.build()

            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(T::class.java)
    }

    fun updateToken(newToken: String) {
        baseInterceptor.setToken("Bearer $newToken")
    }
}
