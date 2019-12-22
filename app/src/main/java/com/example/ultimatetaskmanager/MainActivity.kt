package com.example.ultimatetaskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

       super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        add_task.setOnClickListener({
            val intent = Intent(it.context,TaskFormActivity::class.java)
            startActivity(intent)
        })



    }
}
