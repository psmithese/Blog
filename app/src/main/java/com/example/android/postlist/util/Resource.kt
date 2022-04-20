package com.example.android.postlist.util

// Generic Resource Class to handle network errors
//sealed class Resource<T>(val data: T? = null, val message: String? = null) {
//    class Success<T>(data: T) : Resource<T>(data)
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
//    class Loading<T>(data: T? = null) : Resource<T>(data)
//}

sealed class Resource<out T>(val data: T? = null) {

    class Success<out T>(data: T): Resource<T>(data)

    data class Error(val exception: Exception): Resource<Nothing>()

    object Loading: Resource<Nothing>()


}