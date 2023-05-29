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
import com.example.socialsphere.databinding.ActivitySignUpBinding
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var signUpBinding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        signUpBinding.btnCreateAccount.setOnClickListener {
            Log.d("FIRST", "FIRST CLICK")
            verifyDetails()
            signUp()
            CoroutineScope(Dispatchers.Main).launch{
                authenticationViewModel.signUpFlow.collect{
                    when(it){
                        is ResponseState.Error -> {
                            Toast.makeText(this@SignUpActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                            signUpBinding.signUpProgressBar.visibility = View.GONE
                        }
                        is ResponseState.Loading -> {
                            signUpBinding.signUpProgressBar.visibility = View.VISIBLE
                            Log.d("SECOND", "SECOND CLICK")
                        }
                        is ResponseState.Success -> {
                            Log.d("SUCCESS", "SUCCESS CLICK")
                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                            signUpBinding.signUpProgressBar.visibility = View.GONE
                            Toast.makeText(this@SignUpActivity, "Login Success", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        null -> { Toast.makeText(this@SignUpActivity, "Exception Occured", Toast.LENGTH_SHORT).show()}
                    }
                }
            }
        }


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

    private fun signUp(){
        val name = signUpBinding.editTextName.text.toString()
        val email = signUpBinding.editTextTextEmailAddress.text.toString()
        val password = signUpBinding.editTextTextPassword.text.toString()
        val confirmPassword = signUpBinding.editTextConfirmPassword.text.toString()

        authenticationViewModel.signUp(name, email, password)
        Log.d("ON_CLICK", "$name, $email, $password")


    }

    private fun checkStatus(){
//        signUpFlow.let {
//            when(it){
//                is ResponseState.Error -> {
//                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
//                    signUpBinding.signUpProgressBar.visibility = View.GONE
//                }
//                is ResponseState.Loading -> {
//                    signUpBinding.signUpProgressBar.visibility = View.VISIBLE
//                    Log.d("SECOND", "SECOND CLICK")
//                }
//                is ResponseState.Success -> {
//                    Log.d("SUCCESS", "SUCCESS CLICK")
//                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    signUpBinding.signUpProgressBar.visibility = View.GONE
//                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//                null -> TODO()
//            }
//        }

    }
}