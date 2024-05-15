package com.cuongngo.core_project.data.database.data_source

import android.util.Log
import com.cuongngo.core_project.services.network.BaseResult

abstract class BaseLocalDataSource {
    protected suspend fun <T> getResult(call: suspend () -> T): BaseResult<T> {
        return try {
            val result = call()
            BaseResult.success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): BaseResult<T> {
        Log.e("BaseLocalDataSource", "Local data call has failed for a following reason: $message")
        return BaseResult.error("Local data call has failed for a following reason: $message")
    }
}