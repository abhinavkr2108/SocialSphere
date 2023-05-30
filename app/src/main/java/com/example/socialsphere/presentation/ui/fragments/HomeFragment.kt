package com.example.socialsphere.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialsphere.R
import com.example.socialsphere.databinding.FragmentHomeBinding
import com.example.socialsphere.domain.models.UserPost
import com.example.socialsphere.presentation.adapters.UserPostAdapter
import com.example.socialsphere.presentation.ui.activities.PostQuestionActivity
import com.example.socialsphere.presentation.viewmodels.AddPostViewModel
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import com.example.socialsphere.presentation.viewmodels.CommonViewModel
import com.example.socialsphere.presentation.viewmodels.RetrievePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var userPostAdapter: UserPostAdapter
    private lateinit var calendar: Calendar
    private val commonViewModel by activityViewModels<CommonViewModel>()
    private val retrievePostViewModel by viewModels<RetrievePostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeBinding = FragmentHomeBinding.bind(view)
        userPostAdapter= UserPostAdapter()
        calendar = Calendar.getInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding.fabAddPost.setOnClickListener {
            val intent = Intent(requireContext(), PostQuestionActivity::class.java)
            startActivity(intent)
        }

        retrievePosts()
        setUpRecyclerView()
    }

    private fun retrievePosts() {
        // Getting date and time via calendar instance
        val currentDate = SimpleDateFormat("dd-MMMM-yyyy").format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calendar.time)

//        // Initializing userPost properties
//        val name = commonViewModel.name.value
//        val postKey = commonViewModel.postKey.value.toString()
//        val post = commonViewModel.post.value.toString()
//        val time = commonViewModel.time.value.toString()
//
//        Log.d("USER_POST", "[$name $postKey $post $time]")
//
//        val userPost = UserPost(name, postKey, post, time)
//        val postList = arrayListOf<UserPost>()

        CoroutineScope(Dispatchers.Main).launch{
            val name = commonViewModel.name.collect().toString()
            val postKey = commonViewModel.postKey.collect().toString()
            val post = commonViewModel.post.collect().toString()
            val time = commonViewModel.time.collect().toString()

            Log.d("USER_POST", "[$name $postKey $post $time]")

            val userPost = UserPost(name, postKey, post, time)
            val postList = arrayListOf<UserPost>()
            retrievePostViewModel.retrievePost(postList, userPost)
            retrievePostViewModel.retrieveStatus.collect{
                when {
                    it.isLoading ->{
                        homeBinding.homeProgressbar.visibility = View.VISIBLE
                    }
                    it.dataRetrieved ->{
                        homeBinding.homeProgressbar.visibility = View.GONE
                        userPostAdapter.differ.submitList(postList as List<UserPost>)
                        Log.d("ADAPTER", "$postList")
                    }
                    it.errorMessage.isNotBlank() ->{
                        Toast.makeText(requireContext(), "${it.errorMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        homeBinding.rcvPosts.apply {
            hasFixedSize()
            adapter = userPostAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}