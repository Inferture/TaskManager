package com.example.ultimatetaskmanager

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.example.ultimatetaskmanager.databinding.ItemTaskBinding
import com.example.ultimatetaskmanager.network.Api
import com.example.ultimatetaskmanager.network.TasksRepository
import kotlinx.android.synthetic.main.fragment_task_form.view.*
import kotlinx.coroutines.*


class TasksAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {



    lateinit var context: Context;
    private val tasksRepository = TasksRepository()

    private val coroutineScope = MainScope()

    private lateinit var updateJob: Job

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.i("MyStuff", "BindView, tasks length:" + tasks.count())

        TasksAdapter.holder=holder

        holder.bind(tasks[position], position)

        holder.itemView.delete_task.setOnClickListener(
            {
                notifyItemRemoved(position)
                onDeleteTaskListener(tasks[position])
            })
        holder.itemView.edit_task.setOnClickListener(
            {
                onEditTaskListener(position)
            })
        holder.itemView.task_title.setOnLongClickListener(
            {
                share(holder);
            })
        holder.itemView.task_id.setOnLongClickListener(
            {
                share(holder);
            })
        holder.itemView.task_description.setOnLongClickListener(
            {
                share(holder);
            })

        updateJob = coroutineScope.launch {

        }

    }

    companion object
    {
        lateinit var holder: TaskViewHolder

    }

    fun share(holder: TaskViewHolder):Boolean
    {
        Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, holder.itemView.task_title.text.toString() + "\n" + holder.itemView.task_description.text.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(context,shareIntent,null)
        return true
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        parent.contraintLayoutTaskFormFragment
        context=parent.context

        return TaskViewHolder(DataBindingUtil.inflate<ItemTaskBinding>(LayoutInflater.from(parent.getContext()),
            R.layout.item_task, parent, false))

    }


    override fun getItemCount(): Int {
        return tasks.count()
    }

    fun onDeleteTaskListener(task:Task)
    {
        tasks.remove(task)



        updateJob.cancel()

        updateJob = coroutineScope.launch {
            delay(500)
            notifyDataSetChanged()
        }

        coroutineScope.launch {

            Api.INSTANCE.tasksService.deleteTask(task.id)

        }


    }


    fun onEditTaskListener(taskNum:Int)
    {
        notifyDataSetChanged()
        MainFragment.navController.navigate(MainFragmentDirections.actionMainFragmentToTaskFormActivity(taskNum,  tasks[taskNum].id,  tasks[taskNum].title, tasks[taskNum].description))
    }


}