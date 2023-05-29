package com.example.socialsphere.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityLoginBinding
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginBinding.btnLogin.setOnClickListener {
            verifyDetails()
            loginUser()
            checkStatus()
        }
    }

    private fun verifyDetails(){
        if (loginBinding.editTextTextEmailAddress.text.isEmpty() or loginBinding.editTextTextEmailAddress.text.isBlank()){
            loginBinding.editTextTextEmailAddress.error = "Email Cannot be Empty"
        }

        if (loginBinding.editTextTextPassword.text.isEmpty() or loginBinding.editTextTextPassword.text.isBlank()){
            loginBinding.editTextTextPassword.error = "Password Cannot be Empty"
        }
    }

    private fun loginUser(){
        val email = loginBinding.editTextTextEmailAddress.text.toString()
        val password = loginBinding.editTextTextPassword.text.toString()

        authenticationViewModel.login(email, password)
    }
    
    private fun checkStatus(){
        CoroutineScope(Dispatchers.Main).launch{
            authenticationViewModel.loginFlow.collect{
                when(it){
                    is ResponseState.Error -> {
                        Toast.makeText(this@LoginActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                        loginBinding.loginProgressBar.visibility = View.GONE
                    }
                    is ResponseState.Loading -> {
                        loginBinding.loginProgressBar.visibility = View.VISIBLE
                        Log.d("SECOND", "SECOND CLICK")
                    }
                    is ResponseState.Success -> {
                        Log.d("SUCCESS", "SUCCESS CLICK")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        loginBinding.loginProgressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    null -> { Toast.makeText(this@LoginActivity, "Exception Occured", Toast.LENGTH_SHORT).show()}
                }
            }
        }
    }
}