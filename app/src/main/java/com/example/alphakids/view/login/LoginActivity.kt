package com.example.alphakids.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import com.example.alphakids.MainActivity
import com.example.alphakids.R
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.data.response.LoginResponsee
import com.example.alphakids.databinding.ActivityLoginBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.customview.EditTextEmail
import com.example.alphakids.view.customview.EditTextPassword
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var editTextEmail: EditTextEmail
    private lateinit var editTextPassword: EditTextPassword

    private val loginViewModel by viewModels<LoginViewModel>(){
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextPassword = findViewById(R.id.passwordEditText)
        editTextEmail = findViewById(R.id.emailEditText)

        userPreference = UserPreference(dataStore)

        editTextEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        editTextPassword.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        setupAction()
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.login(email, password)
            ViewModelFactory.clearInstance()
            loginViewModel.saveSession(UserModel(email, ""))
        }

        loginViewModel.isLoadingLogin.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        loginViewModel.loginResponse.observe(this) { response ->
            Log.d("LoginActivity", "receive response: $response")
            response?.let {
                if (it.error == true){
                    showUserCreated(false, it)
                } else{
                    showLoading(false)

                    it.data?.token?.let{ token ->
                        lifecycle.coroutineScope.launch {
                            userPreference.saveToken(token)
                            Log.d("LoginActivity", "Token saved: $token")
                        }
                        showUserCreated(true, it)
                    }
                }
            }
        }

        loginViewModel.errorMessage.observe(this){errorMessage ->
            if (errorMessage != null){
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserCreated(isSuccess: Boolean, response: LoginResponsee?) {
        val title = if (isSuccess) "Yeah!" else "Oops!"
        val message = if (isSuccess) "Login berhasil. Selamat datang di halaman utama." else response?.message ?: "Login gagal. Silahkan coba lagi."

        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                if (isSuccess) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }.create().show()
        showLoading(false)
    }

}