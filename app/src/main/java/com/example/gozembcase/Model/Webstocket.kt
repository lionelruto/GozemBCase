package com.example.gozembcase.Model

import java.io.Serializable
import java.util.Objects

data class Webstocket(

    val type: String,

    val content: HashMap<String, String>
): Serializable
