package com.example.ultimatetaskmanager.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>


    @PATCH("users")
    suspend fun updateUser(@Body user: UserInfo): Response<UserInfo>

}