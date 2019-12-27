package com.example.ultimatetaskmanager

import android.app.Application
import com.example.ultimatetaskmanager.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}