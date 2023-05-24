package com.example.socialsphere.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialsphere.domain.repository.SocialSphereRepository
import com.example.socialsphere.utils.ResponseState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val repository: SocialSphereRepository): ViewModel() {
    private val _loginFlow = MutableStateFlow<ResponseState<FirebaseUser>?>(null)
    val loginFlow: StateFlow<ResponseState<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableStateFlow<ResponseState<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<ResponseState<FirebaseUser>?> = _signUpFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser!= null){
            _loginFlow.value = ResponseState.Success(currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = ResponseState.Loading()
        _loginFlow.value = repository.login(email, password)
    }

    fun signUp(name: String, email: String, password: String)= viewModelScope.launch{
        _signUpFlow.value = ResponseState.Loading()
        _signUpFlow.value = repository.signUp(name, email, password)
    }

    fun logout() = viewModelScope.launch {
        repository.logout()
        _loginFlow.value = null
        _signUpFlow.value = null
    }
}