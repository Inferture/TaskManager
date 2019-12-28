package com.example.ultimatetaskmanager

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.ActivityTaskFormBinding
import kotlinx.android.synthetic.main.activity_task_form.*

class TaskFormActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTaskFormBinding


    private val tasksViewModel by lazy {
        ViewModelProviders.of(this).get(TasksViewModel::class.java)
    }

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_form)

        var numTask = -1;


        var args = TaskFormActivityArgs.fromBundle(intent?.extras!!)
        numTask = args.numTask


        id=args.id
        if(numTask>=0)
        {
            binding.currentTask=Task(id, args.title!!, args.description!!)
            binding.validateTask.setText("Modify")
        }
        else
        {
            binding.currentTask=Task(id, "", "")
        }


        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    var intentText = intent.getStringExtra(Intent.EXTRA_TEXT)


                    var intentTitle = intentText.split('\n')[0]
                    var intentDescription=""
                    if(intentText.split('\n').size>1)
                    {
                        intentDescription= intentText.split('\n')[1]
                    }
                    id="outsideId"//Temporary
                    binding.currentTask=Task(id, intentTitle, intentDescription)
                }
            }
        }

        binding.validateTask.setOnClickListener({

            binding.apply {
                currentTask=Task(id,editTitle.text.toString(), editDescription.text.toString())
                if(numTask<0)
                {
                    AddTask(currentTask!!)
                }
                else
                {
                    EditTask(currentTask!!)
                }
            }
            nav_host_fragment_task_form.findNavController().navigate(R.id.action_taskFormFragment_to_mainActivity3)

        })

        binding.backFromAddTask.setOnClickListener({

            if(binding.editTitle.text.toString()!="" || binding.editDescription.text.toString()!="")
            {
                val builder = AlertDialog.Builder(this)
                val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                    nav_host_fragment_task_form.findNavController().navigate(R.id.action_taskFormFragment_to_mainActivity3)
                }
                val negativeButtonClick = {dialog: DialogInterface, which: Int ->
                }
                with(builder)
                {
                    setTitle("Confirm")
                    setMessage("Modifications will be lost.")
                    setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                    setNegativeButton(android.R.string.no, DialogInterface.OnClickListener(function = negativeButtonClick))
                    show()
                }
            }
            else
            {
                nav_host_fragment_task_form.findNavController().navigate(R.id.action_taskFormFragment_to_mainActivity3)
            }
        })
        binding.invalidateAll()
    }


    fun AddTask(title:String, description: String)
    {
        tasksViewModel.createTask("lel",title,description)
    }
    fun AddTask(task:Task)
    {
        tasksViewModel.createTask(task)
    }
    fun EditTask(task:Task)
    {
        tasksViewModel.updateTask(task)
    }
    fun EditTask(numTask: Int, title:String, description: String)
    {
        Log.i("MyStuff", "Editing");
        Log.i("MyStuff", "Id:" + id);
        Log.i("MyStuff", "Title:" + title);
        Log.i("MyStuff", "Description:" + description);
        tasksViewModel.updateTask(id,title,description)
    }
}
