package com.example.socialsphere.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.socialsphere.R
import com.example.socialsphere.databinding.ActivityMainBinding
import com.example.socialsphere.databinding.FragmentAccountBinding
import com.example.socialsphere.presentation.ui.activities.IntroActivity
import com.example.socialsphere.presentation.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var accountBinding: FragmentAccountBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        accountBinding = FragmentAccountBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountBinding.btnLogout.setOnClickListener {
            authenticationViewModel.logout()
            startActivity(Intent(context, IntroActivity::class.java))
            activity?.finish()
        }
    }
}