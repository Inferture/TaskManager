package com.example.ultimatetaskmanager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(task: Task) {
        itemView.task_id.setText(task.id);
        itemView.task_title.setText(task.title);
        itemView.task_description.setText(task.description);
    }
}