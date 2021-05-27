package com.example.pangchat.message.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class MessageResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : MessageResult<T>()
    data class Error(val exception: Exception) : MessageResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}