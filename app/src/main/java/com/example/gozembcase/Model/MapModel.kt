package com.example.gozembcase.Model

import java.io.Serializable

data class MapModel(

    val type: String,

    val content: HashMap<String, String>
): Serializable
