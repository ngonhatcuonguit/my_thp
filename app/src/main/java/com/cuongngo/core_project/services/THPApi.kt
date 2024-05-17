package com.cuongngo.core_project.services

import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.invoker.ApiClientFactory
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
interface THPApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun loginWithAccount(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant_type: String,
    ): Response<LoginResponse>

//    @GET("/api/Order/MGetProduct")
//    suspend fun getProduct(
//        @Query("sale_org") sale_org: Int  = 4100
//    ): Response<>
    @GET("/api/News/MGetHotNews")
    suspend fun getHotNew(
    ): Response<HotNewResponse>

    companion object {
        operator fun invoke(): THPApi {
            return ApiClientFactory.createService()
        }
    }
}