package com.example.alphakids.retrofit

import com.example.alphakids.data.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PredictApiService {
    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part image: MultipartBody.Part,
    ): PredictResponse
}