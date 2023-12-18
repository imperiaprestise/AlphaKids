package com.example.alphakids.data

import com.example.alphakids.data.response.PredictResponse
import com.example.alphakids.retrofit.PredictApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.await
import java.io.File

class PredictRepository(private val predictApiService: PredictApiService) {

    suspend fun predictAlphabet(imageFile: MultipartBody.Part) : PredictResponse {
        return predictApiService.predict(imageFile)
    }
}