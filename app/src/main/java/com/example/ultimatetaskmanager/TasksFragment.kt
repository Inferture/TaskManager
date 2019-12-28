package com.example.ultimatetaskmanager


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultimatetaskmanager.databinding.FragmentTasksBinding
import com.example.ultimatetaskmanager.network.TasksRepository


class TasksFragment : Fragment() {

    private val tasksViewModel by lazy {
        ViewModelProviders.of(this).get(TasksViewModel::class.java)
    }

    private val tasksRepository = TasksRepository()


    private lateinit var binding: FragmentTasksBinding

    private lateinit var tasksAdapter: TasksAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks,container, false)



        binding.tasksList = TaskList(mutableListOf<Task>())

        tasksAdapter = TasksAdapter(binding.tasksList!!.tasks)



        var tasksArrayList = savedInstanceState?.getParcelableArrayList<Task>("tasks")
        if (tasksArrayList != null) {
            binding.tasksList!!.tasks.apply {
                removeAll(this)
                addAll(tasksArrayList)
            }

            binding.tasksRecyclerView.adapter=tasksAdapter
        }

        val extras = activity?.intent?.extras
        if(extras?.getString("title")!=null && extras.getString("description")!=null )
        {
            Log.i("MyStuff","adding task");
            binding.tasksList!!.tasks.add(Task("lel",extras.getString("title"),extras.getString("description")))
        }

        val view = binding.root
        binding.tasksRecyclerView.adapter = tasksAdapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(context)

        return view;
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        tasksViewModel.getTasks().observe(this, Observer {

            if (it != null) {
                binding.tasksList!!.tasks.clear()
                binding.tasksList!!.tasks.addAll(it)
                tasksAdapter.notifyDataSetChanged()
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        var tasksArrayList = ArrayList<Task>(binding.tasksList!!.tasks)
        outState.putParcelableArrayList("tasks", tasksArrayList)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        var tasksArrayList = savedInstanceState?.getParcelableArrayList<Task>("tasks")
        if (tasksArrayList != null) {
            binding.tasksList!!.tasks.removeAll(binding.tasksList!!.tasks)
            binding.tasksList!!.tasks.addAll(tasksArrayList)
            tasksAdapter.notifyDataSetChanged()
        }
    }
}