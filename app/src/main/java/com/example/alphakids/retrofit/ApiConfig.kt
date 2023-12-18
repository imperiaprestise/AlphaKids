package com.example.alphakids.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private var PREDICT_URL : String = "https://hello-web-hrk77c3sea-uc.a.run.app/"
    private var AUTH_URL : String = "https://asia-southeast2-alphakids-407213.cloudfunctions.net/alphakid/"
    fun getApiService(token: String): ApiService{
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val authInterceptor = Interceptor{ chain ->
//            val req = chain.request()
//            val requestHeaders = req.newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .addHeader("Content-Type", "application/json") // Tambahkan ini
//                .build()
//            chain.proceed(requestHeaders)
//        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-southeast2-alphakids-407213.cloudfunctions.net/alphakid/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)

    }

    fun getPredictApiService() : PredictApiService{

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hello-web-hrk77c3sea-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PredictApiService::class.java)
    }
}