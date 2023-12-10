package com.example.alphakids.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alphakids.data.Repository
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.response.ErrorResponse
import com.example.alphakids.data.response.LoginResponsee
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: Repository): ViewModel() {
    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin

    private val _loginResponse = MutableLiveData<LoginResponsee?>()
    val loginResponse: LiveData<LoginResponsee?> = _loginResponse

    private val _loggedInUser = MutableLiveData<UserModel?>()
    val loggedInUser: LiveData<UserModel?> = _loggedInUser

    val errorMessage = MutableLiveData<String?>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoadingLogin.value = true
                val response = repository.login(username, password)
                Log.d("LoginViewModel", "Response : ${response.data}")
                _isLoadingLogin.value = false
                _loginResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                val errorResponse = LoginResponsee(null, true, errorMessage)
                _loginResponse.postValue(errorResponse)
            } catch (E: Exception) {
                val errorText = "an error occured: ${E.message}"
                errorMessage.postValue(errorText)
            }
        }
    }

    fun saveSession(user: UserModel){
        Log.d("LoginViewModel", "Saving session for user: $user")
        viewModelScope.launch {
            repository.saveSession(user)
            _loggedInUser.postValue(user)
            Log.d("LoginViewModel", "Session saved: ${_loggedInUser.value}")

            val username = user.username
            Log.d("LoginViewModel", "Username: $username")
        }
    }
}