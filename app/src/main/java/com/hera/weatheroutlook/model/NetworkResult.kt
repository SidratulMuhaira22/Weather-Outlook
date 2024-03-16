package com.hera.weatheroutlook.model

sealed class NetworkResult<out R> {

    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val localizedMessage: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${localizedMessage}]"
            is Loading -> "Loading"
        }
    }
}