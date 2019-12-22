package com.example.ultimatetaskmanager


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultimatetaskmanager.network.TasksRepository
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*


class TasksFragment : Fragment() {

    //private var model =  TasksViewModel.model
    /*private val tasks = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/
    private val tasksViewModel by lazy {
        ViewModelProviders.of(this).get(TasksViewModel::class.java)
    }

    private val tasksRepository = TasksRepository()
    private val tasks = mutableListOf<Task>()


    private val tasksAdapter= TasksAdapter(tasks)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var tasksArrayList = savedInstanceState?.getParcelableArrayList<Task>("tasks")
        if (tasksArrayList != null) {
            //model.tasks = tasksArrayList.toMutableList()
            tasks.removeAll(tasks)
            tasks.addAll(tasksArrayList)
            tasks_recycler_view.adapter=TasksAdapter(tasks)
        }

        val extras = activity?.intent?.extras
        if(extras?.getString("title")!=null && extras.getString("description")!=null )
        {
            Log.i("MyStuff","adding task");
            tasks.add(Task("lel",extras.getString("title"),extras.getString("description")))
        }

        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        view.tasks_recycler_view.adapter = tasksAdapter
        view.tasks_recycler_view.layoutManager = LinearLayoutManager(context)
        return view;

    }

    override fun onResume() {
        super.onResume()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        tasksViewModel.getTasks().observe(this, Observer {

            if (it != null) {
                tasks.clear()
                tasks.addAll(it)
                tasksAdapter.notifyDataSetChanged()
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        var tasksArrayList = ArrayList<Task>(tasks)
        outState.putParcelableArrayList("tasks", tasksArrayList)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        var tasksArrayList = savedInstanceState?.getParcelableArrayList<Task>("tasks")
       if (tasksArrayList != null) {
            tasks.removeAll(tasks)
            tasks.addAll(tasksArrayList)
            tasks_recycler_view.adapter=TasksAdapter(tasks)
        }


    }
}
