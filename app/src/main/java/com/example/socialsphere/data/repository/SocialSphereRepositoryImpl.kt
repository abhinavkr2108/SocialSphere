package com.example.socialsphere.data.repository

import android.util.Log
import com.example.socialsphere.domain.models.UserPost
import com.example.socialsphere.domain.repository.SocialSphereRepository
import com.example.socialsphere.utils.ResponseState
import com.example.socialsphere.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import javax.inject.Inject

class SocialSphereRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): SocialSphereRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val currentUserId: String?
        get() = currentUser?.uid
    override val firestoreDatabase: FirebaseFirestore?
        get() = FirebaseFirestore.getInstance()
    override val database: FirebaseDatabase
        get() = FirebaseDatabase.getInstance()

    override val documentReference: DocumentReference?
        get() = firestoreDatabase?.collection("user")?.document(currentUserId!!)

    override val key: String
        get() = database.getReference("User Questions").child(currentUserId!!).push().key!!
    override val postKey: String
        get() = database.getReference("All Questions").push().key!!

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

        } catch(e: FirebaseAuthWeakPasswordException) {
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Enter Password greater than 7 characters")

        } catch(e: FirebaseAuthInvalidCredentialsException) {
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Invalid Credentials")

        } catch(e: FirebaseAuthUserCollisionException) {
            e.printStackTrace()
            ResponseState.Error(data = null, message = e.message?: "Email already Exists")
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): ResponseState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Log.d("RSIGNUP", "$name $email $password")
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

    override suspend fun uploadPost(post: UserPost) {
        val userQuestions = database.getReference("User Questions").child(currentUserId!!)
        userQuestions.child(key).setValue(post)
        val allQuestions = database.getReference("All Questions")
        allQuestions.child(postKey).setValue(post)
    }

    override suspend fun retrievePost(list: ArrayList<UserPost>, post: UserPost) {
        val allQuestions = database.getReference("All Questions")
        allQuestions.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (snapshot in snapshot.children){
                    list.add(post)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DB_ERROR", "${error}")
            }
        })
    }
}