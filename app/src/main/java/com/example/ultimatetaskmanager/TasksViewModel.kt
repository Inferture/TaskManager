package com.example.ultimatetaskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ultimatetaskmanager.network.TasksRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class TasksViewModel: ViewModel() {

    private val coroutineScope = MainScope()

    private val tasksRepository = TasksRepository()
    private val tasks = mutableListOf<Task>()

    fun getTasks(): LiveData<List<Task>?> {
        val tasks = MutableLiveData<List<Task>?>()

        coroutineScope.launch {
            var tasksList = tasksRepository.loadTasks()
            tasks.postValue(tasksList)
        }
        return tasks
    }

    fun deleteTask(id: String): LiveData<String?> {
        val task = MutableLiveData<String?>()
        coroutineScope.launch { task.postValue(tasksRepository.loadDeleteTask(id)) }
        return task
    }


    fun createTask(id: String, title: String, description:  String): LiveData<Task?> {
        val tasks = MutableLiveData<Task?>()
        coroutineScope.launch { tasks.postValue(tasksRepository.loadCreateTask(id, title, description)) }
        return tasks
    }

    fun createTask(task:Task): LiveData<Task?> {
        val tasks = MutableLiveData<Task?>()
        coroutineScope.launch { tasks.postValue(tasksRepository.loadCreateTask(task)) }
        return tasks
    }

    fun updateTask(id: String, title: String, description:  String): LiveData<Task?> {
        val task = MutableLiveData<Task?>()
        coroutineScope.launch { task.postValue(tasksRepository.loadUpdateTask(id, title, description)) }
        return task
    }

    fun updateTask(task:Task): LiveData<Task?> {
        val taskData = MutableLiveData<Task?>()
        coroutineScope.launch { taskData.postValue(tasksRepository.loadUpdateTask(task)) }
        return taskData
    }
}