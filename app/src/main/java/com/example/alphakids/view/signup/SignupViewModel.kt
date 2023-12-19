package com.example.alphakids.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alphakids.data.Repository
import com.example.alphakids.data.response.ErrorResponse
import com.example.alphakids.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: Repository) : ViewModel() {
    private val _isLoadingSignup = MutableLiveData<Boolean>()
    val isLoadingSignup: LiveData<Boolean> = _isLoadingSignup

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> = _registerResponse

    val errorMessage = MutableLiveData<String?>()
    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            try {
                _isLoadingSignup.value = true
                val response = repository.register(name, email, password)
                _registerResponse.value = response
                _isLoadingSignup.value = false
                _registerResponse.postValue(response)
            } catch (e:HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                val errorResponse = RegisterResponse(true,  errorMessage)
                _registerResponse.postValue(errorResponse)
            } catch (E: Exception){
                val errorText = "an error occurred: ${E.message}"
                errorMessage.postValue(errorText)
            }
        }
    }
}
