package com.example.gozembcase.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gozembcase.Model.MapModel
import com.example.gozembcase.Model.User
import com.example.gozembcase.Model.Webstocket
import com.example.gozembcase.data.Ressource
import com.example.gozembcase.data.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import javax.inject.Inject


class UserRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {
    var isSigned = false
   //private val uz= LiveData<User>()
    var uz : MutableLiveData<User> = MutableLiveData()

    override fun getUser(uid: String,  result: (Ressource<MutableMap<String, Any>?>) ->Unit){
        db.collection("users")
           .document(uid)
           .get()
           .addOnCompleteListener{
               if (it.isSuccessful){
                   result.invoke(Ressource.success(it.result.data))
               }
           }
    }

    override fun getMapModel(uid: String, result: (Ressource<MutableMap<String, Any>?>) -> Unit) {
        db.collection("maps")
            .document(uid)
            .get()
            .addOnCompleteListener{
                if (it.isSuccessful){
                    result.invoke(Ressource.success(it.result.data))
                }
            }
    }

    override fun getWebstocket(uid: String, result: (Ressource<MutableMap<String, Any>?>) -> Unit) {
        db.collection("websocket")
            .document(uid)
            .get()
            .addOnCompleteListener{
                if (it.isSuccessful){
                    result.invoke(Ressource.success(it.result.data))
                }
            }
    }



    override fun inserUser(user: User, mapModel: MapModel, email: String, password: String, websocket: Webstocket,result: (Ressource<String>)->Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val uid = auth.currentUser!!.uid

                    db.collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "createUserCollection:success")

                            db.collection("maps")
                                .document(uid)
                                .set(mapModel)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "createUserCollection:success")

                                    db.collection("websocket")
                                        .document(uid)
                                        .set(websocket)
                                        .addOnSuccessListener { documentReference ->
                                            Log.d(TAG, "createUserCollection:success")

                                            result.invoke(Ressource.success("Utilisateur enregistré"))


                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error adding document", e)
                                        }

                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }

                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                }
            }
    }

    override fun logOut(): Boolean {
        auth.signOut()
        return true
    }

    override fun login(email: String, password: String, result: (Ressource<String>)->Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    result.invoke(Ressource.success(task.result.user!!.uid))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    isSigned=false

                }
            }
    }
}