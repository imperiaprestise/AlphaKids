package com.example.alphakids.di

import android.app.Application
import android.content.Context
import com.example.alphakids.data.PredictRepository
import com.example.alphakids.data.Repository
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository{
        val userPreference = UserPreference.getInstance(context.dataStore)

        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository(apiService, userPreference)
    }

    fun providePredictRepository(context: Context): PredictRepository{
        val apiService = ApiConfig.getPredictApiService()
        return PredictRepository(apiService)
    }
}