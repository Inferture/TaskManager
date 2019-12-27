package com.example.ultimatetaskmanager.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ultimatetaskmanager.Task
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TasksRepository {
    private val tasksService = Api.INSTANCE.tasksService
    private val coroutineScope = MainScope()



    suspend fun loadTasks(): List<Task>? {
        val tasksResponse = tasksService.getTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }



    suspend fun loadDeleteTask(id: String): String? {
        val tasksResponse = tasksService.deleteTask(id)
        Log.e("loadDeleteTasks", tasksResponse.toString())
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }



    suspend fun loadCreateTask(id: String, title: String, description:  String): Task? {
        val tasksResponse = tasksService.createTask(Task(id, title, description))
        Log.e("loadCreateTasks", tasksResponse.toString())
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }



    suspend fun loadUpdateTask(id: String, title: String, description:  String): Task? {
        var task = Task(id, title, description)
        Log.i("loadUpdateTasks: ","Task id:" + task.id);

        val tasksResponse = tasksService.updateTask(task.id, task)

        Log.e("loadUpdateTasks", tasksResponse.toString())

        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

}