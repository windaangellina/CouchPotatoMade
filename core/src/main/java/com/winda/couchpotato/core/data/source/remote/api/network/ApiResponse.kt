package com.winda.couchpotato.core.data.source.remote.api.network

import androidx.annotation.Keep

@Keep
sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Empty : ApiResponse<Nothing>()
}