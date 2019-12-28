package com.example.ultimatetaskmanager.network


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.ultimatetaskmanager.SHARED_PREF_TOKEN_KEY
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_tasks.*

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class Api(val context: Context){


    companion object
    {
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        lateinit var INSTANCE: Api
    }


    fun getToken(): String?{
        //return "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozNiwiZXhwIjoxNjA4MTI0NDE4fQ.KB79Kq8hlRoi_9F9iFqS1aFLK0JZdwFTrt4EjdAXqY4"
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY,null)
    }

    private val moshi = Moshi.Builder().build()

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + getToken())
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    val userService: UserService by lazy { retrofit.create(UserService::class.java) }

    val tasksService: TasksService by lazy { retrofit.create(TasksService::class.java) }

}


