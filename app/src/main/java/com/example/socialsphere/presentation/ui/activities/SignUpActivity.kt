package com.example.socialsphere.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivitySignUpBinding
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.ui.activities.MainActivity
import com.example.socialsphere.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var signUpBinding: ActivitySignUpBinding

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        name = signUpBinding.editTextName.text.toString()
        email = signUpBinding.editTextTextEmailAddress.text.toString()
        password = signUpBinding.editTextTextPassword.text.toString()
        confirmPassword = signUpBinding.editTextConfirmPassword.toString()


        signUpBinding.btnCreateAccount.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                verifyDetails()
                authenticationViewModel.signUp(name, email, password)
                checkStatus()
            }
        })

    }

    private fun verifyDetails() {
        if (signUpBinding.editTextName.text.isBlank()){
            signUpBinding.editTextName.error = "Name Cannot be Empty"
        }

        if (signUpBinding.editTextTextEmailAddress.text.isBlank()){
            signUpBinding.editTextTextEmailAddress.error = "Email Cannot be Empty"
        }

        if (signUpBinding.editTextTextPassword.text.isBlank()){
            signUpBinding.editTextTextPassword.error = "Password Cannot be Empty"
        }

        if (signUpBinding.editTextTextPassword.text.toString().length < 7){
            Toast.makeText(this, "Choose password greater than 7 characters", Toast.LENGTH_SHORT).show()
        }

        if (signUpBinding.editTextConfirmPassword.text.isBlank()){
            signUpBinding.editTextConfirmPassword.error = "Please write the confirm password too"
        }

        if (signUpBinding.editTextTextPassword.text.toString() != signUpBinding.editTextConfirmPassword.text.toString()){
            Toast.makeText(this, "Password and Confirm Password Do not match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkStatus(){
        val loginFlow = authenticationViewModel.loginFlow.value
        loginFlow.let {
            when(it){
                is ResponseState.Error -> {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    signUpBinding.signUpProgressBar.visibility = View.GONE
                }
                is ResponseState.Loading -> {
                    signUpBinding.signUpProgressBar.visibility = View.VISIBLE
                }
                is ResponseState.Success -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    signUpBinding.signUpProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}