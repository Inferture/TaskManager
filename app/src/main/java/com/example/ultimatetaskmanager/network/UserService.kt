package com.example.ultimatetaskmanager.network

import com.example.ultimatetaskmanager.LoginForm
import com.example.ultimatetaskmanager.SignupForm
import com.example.ultimatetaskmanager.TokenResponse
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

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<TokenResponse>

    @POST("users/sign_up")
    suspend fun signup(@Body user: SignupForm): Response<TokenResponse>

}