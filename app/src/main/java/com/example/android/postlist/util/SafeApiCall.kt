package com.example.android.postlist.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SafeApiCall {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }
}
