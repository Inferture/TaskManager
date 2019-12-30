package com.example.ultimatetaskmanager.network

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import okhttp3.MultipartBody

data class UserInfo(

    @field:Json(name = "firstname")
    var firstName: String,
    @field:Json(name = "lastname")
    var lastName: String,
    @field:Json(name = "email")
    var email: String,



    @field:Json(name = "avatar")
    var avatar: String?
)