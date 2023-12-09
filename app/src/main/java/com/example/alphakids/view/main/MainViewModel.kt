package com.example.alphakids.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.alphakids.data.Repository
import com.example.alphakids.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch{
            repository.logout()
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
}