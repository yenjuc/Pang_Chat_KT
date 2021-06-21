package com.example.pangchat.fragment.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pangchat.R
import com.example.pangchat.fragment.data.LoggedUser
import com.example.pangchat.fragment.data.LoginDataSource
import com.example.pangchat.fragment.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedUserView(
        val displayName: String,
        val userId: String
        //... other data fields that may be accessible to the UI
)

/**
 * Authentication result : success (user details) or error message.
 */
data class LogResult(
        val success: LoggedUserView? = null,
        val error: Int? = null
)

/**
 * Data validation state of the login form.
 */
data class LoginFormState(val usernameError: Int? = null,
                          val passwordError: Int? = null,
                          val isDataValid: Boolean = false)

class LoginViewModel(private val loginDataSource: LoginDataSource) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LogResult>()
    val loginResult: LiveData<LogResult> = _loginResult

    private val _logonResult = MutableLiveData<LogResult>()
    val logonResult: LiveData<LogResult> = _logonResult

    fun login(username: String, password: String) {
        MainScope().launch {

            val result: Result<LoggedUser>

            withContext(Dispatchers.IO) {
                result = loginDataSource.login(username, password)
            }

            if (result is Result.Success) {
               _loginResult.value = LogResult(success = LoggedUserView(displayName = result.data.username, userId = result.data.userId))
            } else {
                _loginResult.value = LogResult(error = R.string.login_failed)
            }
        }
    }

    fun logon(username: String, password: String) {
        MainScope().launch {

            val result: Result<LoggedUser>

            withContext(Dispatchers.IO) {
                result = loginDataSource.logon(username, password)
            }

            if (result is Result.Success) {
                _logonResult.value = LogResult(success = LoggedUserView(displayName = result.data.username, userId = result.data.userId))
            } else {
                _logonResult.value = LogResult(error = R.string.logon_failed)
            }
        }
    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}