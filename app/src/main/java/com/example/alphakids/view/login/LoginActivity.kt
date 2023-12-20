package com.example.alphakids.view.login

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
import com.example.alphakids.view.main.MainActivity
import com.example.alphakids.R
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.data.response.LoginResponsee
import com.example.alphakids.databinding.ActivityLoginBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.customview.EditTextPassword
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var editTextPassword: EditTextPassword

    private val loginViewModel by viewModels<LoginViewModel>(){
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userPreference: UserPreference

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextPassword = findViewById(R.id.passwordEditText)

        userPreference = UserPreference(dataStore)

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
            username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.login(username, password)
            ViewModelFactory.clearInstance()
            loginViewModel.saveSession(UserModel(username,"","", ""))
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
                            val username = it.data?.username
                            val dateJoined = it.data?.date
                            val token = it.data?.token
                            val email = it.data?.email

                            val user = UserModel(username ?: "", token ?: "", dateJoined ?: "", email ?: "")
                            userPreference.saveSession(user)
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
        val message = if (isSuccess) getString(R.string.login_berhasil) else response?.message ?: getString(R.string.login_failed)

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