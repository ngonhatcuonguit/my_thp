package com.cuongngo.core_project.services

import com.cuongngo.core_project.data.database.roomdb.entity.FormResponse
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.core_project.response.base.AppBaseResponse
import com.cuongngo.core_project.response.login_response.ApiResponse
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.invoker.ApiClientFactory
import com.cuongngo.core_project.services.network.invoker.NetworkConnectionInterceptor
import com.cuongngo.core_project.ui.event_thp.model.ExamResponse
import com.cuongngo.core_project.ui.event_thp.model.ExaminersResponse
import com.cuongngo.core_project.ui.event_thp.model.UpdateScoreResponse
import com.cuongngo.core_project.ui.request_detail.PushRequestCodeBody
import com.cuongngo.core_project.ui.request_detail.GetRequestStatusResponse
import com.cuongngo.core_project.ui.request_detail.PushRequestResponse
import com.cuongngo.core_project.ui.request_detail.RequestBodyPush
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface THPApi {
    @FormUrlEncoded
    @POST("api/account/gettoken")
    suspend fun loginWithAccount(
        @Field("UserName") username: String,
        @Field("Password") password: String,
        @Field("device_code") device_code: String,
    ): Response<AppBaseResponse<LoginResponse>>

    @FormUrlEncoded
    @POST("api/account/device")
    suspend fun activeDevice(
        @Field("id") device_id: String,
        @Field("manufacturer") manufacturer: String?,
        @Field("model") model: String?,
        @Field("brand") brand: String?,
        @Field("product") product: String?,
        @Field("osVersion") os_version: String?,
        @Field("apiLevel") apiLevel: String?,
        @Field("hardware") hardware: String?,
        @Field("user") user: String?,
        @Field("host") host: String?,
        @Field("display") display: String?,
        @Field("device") device: String?,
    ): Response<ApiResponse>


    @POST("api/request/push-request")
    suspend fun pushRequest(
        @Body requestBodyPush: List<RequestBodyPush>,
    ): Response<PushRequestResponse>

    @GET("api/employee/list")
    suspend fun getListUser(
        @Query("isGetAll") sale_org: Boolean = true
    ): Response<UserTHPResponse>

    @GET("/api/News/MGetHotNews")
    suspend fun getHotNew(
    ): Response<HotNewResponse>

    @GET("api/formstructure/SyncStructure")
    suspend fun getListForm(
        @Query("isGetAll") isGetAll: Boolean? = true,
        @Query("structureID") structureID: Int? = null
    ): Response<FormResponse>
    @GET("api/getexaminer")
    suspend fun getListGK(): Response<AppBaseResponse<ExaminersResponse>>
    @GET("api/getexam")
    suspend fun getTietMuc(): Response<AppBaseResponse<ExamResponse>>

    @GET("api/updatescore")
    suspend fun updateScore(
        @Query("ExaminerId") examinerId: Int?,
        @Query("ExamId") examId: Int?,
        @Query("Score") score: Float?,
        @Query("ComposingScore") composingScore: Float?,
    ): Response<AppBaseResponse<UpdateScoreResponse>>

    @POST("api/request/get-request-update")
    suspend fun getRequestStatus(
        @Body requestCodeBody: PushRequestCodeBody,
    ): Response<GetRequestStatusResponse>


    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor? = null): THPApi {
            return ApiClientFactory.createService(networkConnectionInterceptor)
        }

        fun updateToken(newToken: String) {
            ApiClientFactory.updateToken(newToken)
        }
    }
}