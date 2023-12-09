package com.example.alphakids.data

import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.response.LoginResponsee
import com.example.alphakids.data.response.RegisterResponse
import com.example.alphakids.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class Repository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    suspend fun register(username: String, email: String, password: String) : RegisterResponse{
        return apiService.register(username, email, password)
    }

    suspend fun login(username: String, password: String): LoginResponsee{
        return apiService.login(username, password)
    }
}