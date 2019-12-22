package com.example.ultimatetaskmanager.network

import com.squareup.moshi.Json
import okhttp3.MultipartBody

data class UserInfo(

    @field:Json(name = "firstname")
    val firstName: String,
    @field:Json(name = "lastname")
    val lastName: String,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "avatar")
    val avatar: String
)