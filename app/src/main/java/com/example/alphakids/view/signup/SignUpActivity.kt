package com.example.alphakids.view.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import com.example.alphakids.R
import com.example.alphakids.data.response.RegisterResponse
import com.example.alphakids.databinding.ActivitySignUpBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.customview.EditTextEmail
import com.example.alphakids.view.customview.EditTextPassword
import com.example.alphakids.view.login.LoginActivity
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var editTextPassword: EditTextPassword
    private lateinit var editTextEmail: EditTextEmail
    private lateinit var passwordTextView: TextView

    private val signupViewModel by viewModels<SignupViewModel>(){
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextPassword = findViewById(R.id.passwordEditText)
        editTextEmail = findViewById(R.id.emailEditText)
        passwordTextView = findViewById(R.id.passwordTextView)

        editTextPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

        editTextEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

        setupAction()
    }

    private fun setupAction(){
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
                setMessage(this@SignUpActivity, getString(R.string.incomplete))
            } else {
                lifecycle.coroutineScope.launch {
                    try {
                        signupViewModel.register(username, email, password)
                    } catch (e: Exception) {
                        setMessage(this@SignUpActivity, "Registration failed: ${e.message}")
                    }
                }
            }
        }

        signupViewModel.isLoadingSignup.observe(this) {
            showLoading(it)
        }

        signupViewModel.registerResponse.observe(this) { response ->
            Log.d("SignUpActivity", "Received response: $response")
            response?.let {
                if (it.error == true) {
                    showUserCreated(false, it)
                } else {
                    showLoading(false)
                    response.message?.let { setMessage(this@SignUpActivity, it) }
                    showUserCreated(true, it)
                }
            }
        }

        signupViewModel.errorMessage.observe(this){ errorMessage ->
            if (errorMessage != null){
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMessage(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserCreated(isSuccess: Boolean, response: RegisterResponse?){
        val title = if (isSuccess) "Yeah!" else "Oops!"
        val message = if (isSuccess) getString(R.string.sucsess) else response?.message ?: getString(R.string.signup_failed)

        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                if (isSuccess) {
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.create().show()
    }
}
