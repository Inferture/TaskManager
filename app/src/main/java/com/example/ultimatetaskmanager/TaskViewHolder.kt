package com.example.ultimatetaskmanager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimatetaskmanager.databinding.ItemTaskBinding
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(val binding:ItemTaskBinding/*itemView: View*/) : RecyclerView.ViewHolder(binding.root) {


    fun bind(task: Task) {
        binding.task=task
    }
}