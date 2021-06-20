package com.example.pangchat.user.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class UserResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : UserResult<T>()
    data class Error(val exception: Exception) : UserResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}