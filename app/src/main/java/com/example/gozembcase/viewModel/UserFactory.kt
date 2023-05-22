package com.example.gozembcase.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gozembcase.repository.UserRepository

class UserFactory(private val userRepository: UserRepository ?) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T> ): T {
        return (userRepository?.let { UserViewModel(it) } as T)

    }
}