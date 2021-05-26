package com.example.pangchat.chat.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ChatResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ChatResult<T>()
    data class Error(val exception: Exception) : ChatResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}