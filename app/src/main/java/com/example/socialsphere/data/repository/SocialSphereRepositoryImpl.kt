package com.example.socialsphere.data.repository

import com.example.socialsphere.domain.repository.SocialSphereRepository
import com.example.socialsphere.utils.ResponseState
import com.example.socialsphere.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.IOException
import javax.inject.Inject

class SocialSphereRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): SocialSphereRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): ResponseState<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ResponseState.Success(result.user!!)
        } catch (e: Exception){
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Unknown Error Occurred! ")
        } catch (e: IOException){
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Internet Connection error")
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): ResponseState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            ResponseState.Success(result.user!!)
        } catch (e: Exception){
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Unknown Error Occurred! ")
        } catch (e: IOException){
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Internet Connection error")
        }
    }


    override fun logout() {
        firebaseAuth.signOut()
    }
}