package com.example.pangchat.discover.data
/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DiscoverResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DiscoverResult<T>()
    data class Error(val exception: Exception) : DiscoverResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}