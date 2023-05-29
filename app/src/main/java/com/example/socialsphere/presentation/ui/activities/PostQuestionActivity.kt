package com.example.socialsphere.presentation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityPostQuestionBinding

class PostQuestionActivity : AppCompatActivity() {
    private lateinit var postBinding: ActivityPostQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_question)

        postBinding.addPostArrowBack.setOnClickListener {
            finish()
        }
    }
}