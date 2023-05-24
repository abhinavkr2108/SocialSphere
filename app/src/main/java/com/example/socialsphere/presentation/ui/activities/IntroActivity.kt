package com.example.socialsphere.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityIntroBinding
import com.example.socialsphere.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class IntroActivity : AppCompatActivity() {
    private val user = FirebaseAuth.getInstance().currentUser

    init {
        if (user!= null){
            redirectUser("MAIN")
        }
    }

    private lateinit var introBinding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        // Navigate to Login Activity
        introBinding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        introBinding.signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun redirectUser(name: String) {
        if (name == "MAIN"){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{
            throw Exception("No path exists")
        }
    }
}