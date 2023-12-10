package com.example.alphakids.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.alphakids.data.Repository
import com.example.alphakids.data.entity.UserEntity
import com.example.alphakids.data.pref.UserModel

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    fun getUserData(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
