package com.example.gozembcase.data

import java.lang.Exception

sealed class Ressource<out R>{

    data class success<out R>(val result:R): Ressource<R>()
    data class failure(val exception: Exception): Ressource<Nothing>()
    object loading: Ressource<Nothing>()


}
