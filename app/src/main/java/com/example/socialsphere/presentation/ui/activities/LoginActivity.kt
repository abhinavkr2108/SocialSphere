package com.example.socialsphere.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityLoginBinding
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.ui.activities.MainActivity
import com.example.socialsphere.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var loginBinding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        email = loginBinding.editTextTextEmailAddress.text.toString()
        password = loginBinding.editTextTextPassword.text.toString()

        loginBinding.btnLogin.setOnClickListener {
            verifyDetails()
            authenticationViewModel.login(email, password)
            checkStatus()
        }
    }

    private fun verifyDetails(){
        if (email.isEmpty() or email.isBlank()){
            loginBinding.editTextTextEmailAddress.error = "Email Cannot be Empty"
        }

        if (password.isEmpty() or password.isBlank()){
            loginBinding.editTextTextPassword.error = "Password Cannot be Empty"
        }
    }

    private fun checkStatus(){
        val signUpFlow = authenticationViewModel.signUpFlow.value
        signUpFlow.let {
            when(it){
                is ResponseState.Error -> {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    loginBinding.loginProgressBar.visibility = View.GONE
                }
                is ResponseState.Loading -> {
                    loginBinding.loginProgressBar.visibility = View.VISIBLE
                }
                is ResponseState.Success -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    loginBinding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}