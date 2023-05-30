package com.example.socialsphere.domain.repository

import com.example.socialsphere.domain.models.UserPost
import com.example.socialsphere.utils.ResponseState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

interface SocialSphereRepository {

    val currentUser: FirebaseUser?
    val currentUserId: String?
    val firestoreDatabase: FirebaseFirestore?
    val database: FirebaseDatabase
    val documentReference: DocumentReference?
    val key: String
    val postKey: String

    /**
     * Functions Related to Authentication
     */
    suspend fun login(email: String, password: String): ResponseState<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): ResponseState<FirebaseUser>
    fun logout()

    /**
     * Functions related to Uploading and retrieving posts
     */

    suspend fun uploadPost(post: UserPost)
    suspend fun retrievePost(list: ArrayList<UserPost>, post: UserPost)

}