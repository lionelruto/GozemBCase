package com.example.gozembcase.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gozembcase.Model.MapModel
import com.example.gozembcase.Model.User
import com.example.gozembcase.Model.Webstocket
import com.example.gozembcase.data.Ressource
import com.example.gozembcase.repository.UserRepository
import com.example.gozembcase.webstocket.WebSocketClient
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _getUser = MutableLiveData<Ressource<MutableMap<String, Any>?>>()
    val getUser: LiveData<Ressource<MutableMap<String, Any>?>>
        get() = _getUser
    fun getUser(uid: String){
        //_getUser.value
        userRepository.getUser(uid){_getUser.value = it}
    }

    private val _getMap = MutableLiveData<Ressource<MutableMap<String, Any>?>>()
    val getMap: LiveData<Ressource<MutableMap<String, Any>?>>
        get() = _getMap
    fun getMap(uid: String){
        //_getUser.value
        userRepository.getMapModel(uid){_getMap.value = it}
    }

    private val _getWebsocket = MutableLiveData<Ressource<MutableMap<String, Any>?>>()
    val getWebsocket: LiveData<Ressource<MutableMap<String, Any>?>>
        get() = _getWebsocket
    fun getWebsocket(uid: String){
        //_getUser.value
        userRepository.getWebstocket(uid){_getUser.value = it}
    }

    private val _getInser = MutableLiveData<Ressource<String>>()
    val getInser: LiveData<Ressource<String>>
        get()= _getInser
    fun inserUser (user: User, map: MapModel, email: String, password: String, webSocket: Webstocket){
        userRepository.inserUser(user, map,  email, password, webSocket){_getInser.value= it}
    }

    fun logOut()=userRepository.logOut()

    private val _getLogin = MutableLiveData<Ressource<String>>()
    val getLogin: LiveData<Ressource<String>>
        get()= _getLogin
    fun login(email: String, password: String) {
        userRepository.login(email, password){_getLogin.value= it}
    }
}