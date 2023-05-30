package com.example.socialsphere.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityPostQuestionBinding
import com.example.socialsphere.domain.models.UserPost
import com.example.socialsphere.presentation.ui.fragments.HomeFragment
import com.example.socialsphere.presentation.viewmodels.AddPostViewModel
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.presentation.viewmodels.CommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class PostQuestionActivity : AppCompatActivity() {
    // Initialize Binding
    private lateinit var postBinding: ActivityPostQuestionBinding
    // ViewModels Initialization
    private val addPostViewModel by viewModels<AddPostViewModel>()
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()
    // Calendar Instance
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_question)
        calendar = Calendar.getInstance()

        postBinding.addPostArrowBack.setOnClickListener {
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        postBinding.btnPostQuestion.setOnClickListener {
            postData()
        }
    }

    private fun postData() {
        // Getting date and time via calendar instance
        val currentDate = SimpleDateFormat("dd-MMMM-yyyy").format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calendar.time)

        // Initializing userPost properties
        val name = authenticationViewModel.currentUser?.displayName?:""
        val postKey = addPostViewModel.postKey
        val post = postBinding.inputText.text.toString()
        val time = "${currentDate}: ${currentTime}"

        commonViewModel.setData(name, postKey, post, time)

        // Object of UserPost class
        val userPost = UserPost(
            name = name,
            postKey = postKey,
            post = post,
            time = time
        )

        CoroutineScope(Dispatchers.Main).launch {
            addPostViewModel.uploadPost(userPost)
            addPostViewModel.uploadStatus.collect{
                when{
                    it.isLoading ->{
                        postBinding.postProgressBar.visibility = View.VISIBLE
                    }
                    it.dataLoaded -> {
                        Toast.makeText(this@PostQuestionActivity, "Data Loaded", Toast.LENGTH_SHORT).show()
                        postBinding.postProgressBar.visibility = View.GONE
                    }
                    it.errorMessage.isNotBlank() ->{
                        postBinding.postProgressBar.visibility = View.GONE
                        Toast.makeText(this@PostQuestionActivity, "${it.errorMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
}