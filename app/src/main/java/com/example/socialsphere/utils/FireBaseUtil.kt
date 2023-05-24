package com.example.socialsphere.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await():T{

    return suspendCancellableCoroutine { cont->
        addOnCompleteListener {
            if (it.isComplete){
                cont.resume(it.result, null)
            }
            else if (it.isSuccessful){
                cont.resume(it.result, null)
            }
            else if (it.exception!= null){
                cont.resumeWithException(it.exception!!)
            }
        }
    }

}