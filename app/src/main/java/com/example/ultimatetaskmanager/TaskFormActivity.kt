package com.example.ultimatetaskmanager

import android.content.ClipDescription
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultimatetaskmanager.network.TasksRepository
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task_form.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_task_form.*

class TaskFormActivity : AppCompatActivity() {

    private val tasksViewModel by lazy {
        ViewModelProviders.of(this).get(TasksViewModel::class.java)
    }

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        var numTask = -1;


        var args = TaskFormActivityArgs.fromBundle(intent?.extras!!)//nav_host_fragment_task_form.arguments
        numTask = args.numTask
        id=args.id

        edit_description.setText(args.description)
        edit_title.setText(args.title)


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
                    //nav_host_fragment_task_form
                    edit_title.setText(intentTitle)
                    edit_description.setText(intentDescription)

                    id="outsideId"
                }
            }
        }

        validate_task.setOnClickListener({

            val title = edit_title.text.toString()
            val description = edit_description.text.toString()

            if(numTask<0)
            {
                AddTask(title,description)
            }
            else
            {
                EditTask(numTask,title,description)
            }

            nav_host_fragment_task_form.findNavController().navigate(R.id.action_taskFormFragment_to_mainActivity3)

        })

        back_from_add_task.setOnClickListener({

            if(edit_title.text.toString()!="" || edit_description.text.toString()!="")
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

    }


    fun AddTask(title:String, description: String)
    {
        tasksViewModel.createTask("lel",title,description)
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
