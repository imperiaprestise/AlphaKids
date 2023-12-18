package com.example.alphakids.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alphakids.data.PredictRepository
import com.example.alphakids.view.main.MainViewModel
import com.example.alphakids.data.Repository
import com.example.alphakids.di.Injection
import com.example.alphakids.view.login.LoginViewModel
import com.example.alphakids.view.profile.ProfileViewModel
import com.example.alphakids.view.scan.ScanViewModel
import com.example.alphakids.view.signup.SignupViewModel

class ViewModelFactory(
    private val repository: Repository,
    private val predictRepository: PredictRepository
):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(predictRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown viewmodel class" + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), Injection.providePredictRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun clearInstance(){
            INSTANCE = null
        }
    }
}