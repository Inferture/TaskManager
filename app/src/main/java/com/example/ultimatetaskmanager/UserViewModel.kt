package com.example.ultimatetaskmanager



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ultimatetaskmanager.network.Api
import com.example.ultimatetaskmanager.network.UserInfo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class UserViewModel: ViewModel() {

    private val coroutineScope = MainScope()

    lateinit var userInfo: UserInfo;

    fun updateAvatar(picture: MultipartBody.Part): LiveData<UserInfo?> {
        val infos = MutableLiveData<UserInfo?>()

        coroutineScope.launch {
            var infosUpdate = loadUpdateAvatar(picture)
            infos.postValue(infosUpdate)
        }
        return infos
    }

    suspend fun loadUpdateUser(infos: UserInfo): UserInfo? {

        val userResponse = Api.INSTANCE.userService.updateUser(infos)
        Log.e("loadUpdateAvatar", userResponse.toString())

        return if (userResponse.isSuccessful) userResponse.body() else null
    }


    fun updateUser(user: UserInfo): LiveData<UserInfo?> {
        val infosData = MutableLiveData<UserInfo?>()
        userInfo=user
        coroutineScope.launch {
            var infosUpdate = loadUpdateUser(user)
            infosData.postValue(infosUpdate)
        }
        return infosData
    }

    suspend fun loadUpdateAvatar(picture: MultipartBody.Part): UserInfo? {

        val userResponse = Api.INSTANCE.userService.updateAvatar(picture)
        Log.e("loadUpdateAvatar", userResponse.toString())

        return if (userResponse.isSuccessful) userResponse.body() else null
    }


    fun getPicture(): LiveData<String?> {
        val pic = MutableLiveData<String?>()

        coroutineScope.launch {
            var picture = loadPicture()
            pic.postValue(picture)
        }
        return pic
    }

    suspend fun loadPicture(): String? {
        val tasksResponse = Api.INSTANCE.userService.getInfo()
        return if (tasksResponse.isSuccessful) tasksResponse.body()?.avatar else null
    }


    fun getUserInfos(): LiveData<UserInfo?> {
        val infos = MutableLiveData<UserInfo?>()

        coroutineScope.launch {
            var tasksList = loadUserInfos()
            infos.postValue(tasksList)
        }
        return infos
    }

    suspend fun loadUserInfos(): UserInfo? {
        val tasksResponse = Api.INSTANCE.userService.getInfo()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }
}