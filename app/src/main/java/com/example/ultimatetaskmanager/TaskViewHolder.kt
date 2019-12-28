package com.example.ultimatetaskmanager


import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimatetaskmanager.databinding.ItemTaskBinding

class TaskViewHolder(val binding:ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(task: Task, counter: Int) {
        binding.task=task


        if(counter%2==0)
        {
            var secondColor = ContextCompat.getColor(binding.root.context, R.color.colorListTask2)
            binding.apply {
                taskId.setBackgroundColor(secondColor)
                taskTitle.setBackgroundColor(secondColor)
                taskDescription.setBackgroundColor(secondColor)
                editTask.setBackgroundColor(secondColor)
                deleteTask.setBackgroundColor(secondColor)
            }
        }
    }


}