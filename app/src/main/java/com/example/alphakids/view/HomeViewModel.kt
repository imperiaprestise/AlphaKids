package com.example.alphakids.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    // Metode untuk mengatur nilai email
    fun setEmail(email: String) {
        _email.value = email
    }

    companion object {
        private var instance: HomeViewModel? = null

        // Metode untuk mendapatkan instance HomeViewModel
        fun getInstance(): HomeViewModel {
            if (instance == null) {
                instance = HomeViewModel()
            }
            return instance!!
        }
    }

    // Metode-metode lainnya sesuai kebutuhan
}
