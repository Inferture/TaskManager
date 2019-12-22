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
import com.example.ultimatetaskmanager.network.TasksRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task_form.*
 
class TaskFormActivity : AppCompatActivity() {


    //private val tasksRepository = TasksRepository()

    private val tasksViewModel by lazy {
        ViewModelProviders.of(this).get(TasksViewModel::class.java)
    }

    var id = "" ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        var numTask = -1;

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
                    edit_title.setText(intentTitle)
                    edit_description.setText(intentDescription)

                    id="outsideId"
                }
            }
            else ->
            if (intent?.extras?.getInt("NumTask") != null && intent?.extras?.getString("id") != null &&
                intent?.extras?.getString("title") != null && intent?.extras?.getString("description") != null ) {

                numTask = intent.extras.getInt("NumTask");

                id = intent.extras.getString("id")

                val description = intent?.extras?.getString("description")
                val title  = intent?.extras?.getString("title")
                edit_description.setText(description)
                edit_title.setText(title)


                validate_task.setText("Modify")
            }
            else
            {
                id="newId"
            }
        }

        validate_task.setOnClickListener({
            val intent = Intent(it.context,MainActivity::class.java)


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

            startActivity(intent)

        })

        back_from_add_task.setOnClickListener({

            if(edit_title.text.toString()!="" || edit_title.text.toString()!="")
            {
                val builder = AlertDialog.Builder(this)
                val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                    val intent = Intent(it.context,MainActivity::class.java)
                    startActivity(intent)
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
                val intent = Intent(it.context,MainActivity::class.java)
                startActivity(intent)
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
