package com.example.gozembcase.repository

import androidx.lifecycle.MutableLiveData
import com.example.gozembcase.Model.MapModel
import com.example.gozembcase.Model.User
import com.example.gozembcase.Model.Webstocket
import com.example.gozembcase.data.Ressource

interface UserRepository {

     fun getUser(uid: String, result: (Ressource<MutableMap<String, Any>?>)->Unit)

    fun getMapModel(uid: String,result: (Ressource<MutableMap<String, Any>?>) -> Unit)

    fun getWebstocket(uid: String,result: (Ressource<MutableMap<String, Any>?>) -> Unit)

     fun inserUser (user: User, mapModel: MapModel, email: String, password: String, websocket: Webstocket, result: (Ressource<String>)->Unit)

    fun logOut(): Boolean

    fun login(email: String, password: String, result: (Ressource<String>)->Unit)


}