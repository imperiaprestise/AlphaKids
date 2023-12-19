package com.example.alphakids.data

import com.example.alphakids.data.response.PredictResponse
import com.example.alphakids.retrofit.PredictApiService
import okhttp3.MultipartBody

class PredictRepository(private val predictApiService: PredictApiService) {

    suspend fun predictAlphabet(imageFile: MultipartBody.Part) : PredictResponse {
        return predictApiService.predict(imageFile)
    }
}