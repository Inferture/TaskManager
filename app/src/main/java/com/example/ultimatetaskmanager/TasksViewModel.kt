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
    //private var model =  TasksViewModel.model



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

    fun updateTask(id: String, title: String, description:  String): LiveData<Task?> {
        val task = MutableLiveData<Task?>()
        coroutineScope.launch { task.postValue(tasksRepository.loadUpdateTask(id, title, description)) }
        return task
    }

}


/*
class TasksViewModel : ViewModel() {


    companion object model
    {
        public  val tasks = mutableListOf<Task>(
            Task(id = "id_1", title = "Task 1", description = "description 1"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3"))
    }



}*/