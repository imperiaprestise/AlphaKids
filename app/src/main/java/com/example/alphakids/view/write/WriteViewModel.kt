package com.example.alphakids.view.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alphakids.data.PredictRepository
import com.example.alphakids.data.response.ErrorResponse
import com.example.alphakids.data.response.PredictResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class WriteViewModel(
    private val predictRepository: PredictRepository) : ViewModel() {

    private val _predictResult = MutableLiveData<PredictResponse>()
    val predictResult: LiveData<PredictResponse> = _predictResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val errorMessage = MutableLiveData<String?>()

    fun predictAlphabet(image: File?) {
        viewModelScope.launch {
            try {
                if (image != null){
                    _isLoading.value = true
                    Log.d("ScanViewModel", "File Path: ${image.absolutePath}")
                    val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "image",
                        image.name,
                        requestImageFile
                    )
                    val predictResponse = predictRepository.predictAlphabet(multipartBody)
                    Log.d("ScanViewModel", "Predict Response: $predictResponse")
                    _predictResult.postValue(predictResponse)
                } else{
                    errorMessage.postValue("unknown")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorText = errorResponse.message
                errorMessage.postValue(errorText)
            } catch (e: Exception) {
                val errorText = "Terjadi kesalahan: ${e.message}"
                errorMessage.postValue(errorText)
            }
            finally {
                _isLoading.postValue(false)
            }
        }
    }

}