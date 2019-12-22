package com.example.ultimatetaskmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*



import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.example.ultimatetaskmanager.network.Api
import com.example.ultimatetaskmanager.network.TasksRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task_form.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


class TasksAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {


    lateinit var context: Context;
    private val tasksRepository = TasksRepository()

    private val coroutineScope = MainScope()

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
        holder.itemView.delete_task.setOnClickListener(
            {
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


        context=parent.context
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))

    }


    override fun getItemCount(): Int {
        return tasks.count()
    }

    fun onDeleteTaskListener(task:Task)
    {
        /*
        tasksRepository.deleteTask(task.id)
        tasks.remove(task);
        notifyDataSetChanged()*/
        coroutineScope.launch {
            Api.tasksService.deleteTask(task.id)
            tasks.remove(task)
            notifyDataSetChanged()
        }
    }

    fun onEditTaskListener(taskNum:Int)
    {
        val intent = Intent(context,TaskFormActivity::class.java)

        intent.putExtra("NumTask", taskNum)
        intent.putExtra("id", tasks[taskNum].id)
        intent.putExtra("title", tasks[taskNum].title)
        intent.putExtra("description", tasks[taskNum].description)
        notifyDataSetChanged()
        startActivity(context,intent,null)
    }


}