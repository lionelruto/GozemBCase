package com.example.gozembcase.data

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

//extension de la fonction await pour recuperer les donnés
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if(it.exception != null){
                cont.resumeWithException(it.exception!!)
            }else{
                cont.resume(it.result, null)
            }
        }
    }
}




    /*return suspendCancellableCoroutine { cont->
        addOnCompleteListener{
            if (it.exception !=null){
                cont.resumeWithException(it.exception!!)
            }else{
                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${it.result}")

                cont.resume(it.result, null)
            }
        }
    }

     */
