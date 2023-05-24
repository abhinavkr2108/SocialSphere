package com.example.socialsphere.domain.repository

import com.example.socialsphere.utils.ResponseState
import com.google.firebase.auth.FirebaseUser

interface SocialSphereRepository {

    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): ResponseState<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): ResponseState<FirebaseUser>
    fun logout()

}