package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.sqllite.StudentPerformance.MyListAdapter
import kotlinx.android.synthetic.main.activity_grades.*
import com.example.smartscholar.R;
class Grades : AppCompatActivity() {
    var g_student: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)
        g_student = findViewById(R.id.name_textView)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()
        var intent = intent
        val g_Student_Name = intent.getStringExtra("g_name")
g_student!!.setText(g_Student_Name)
        val g_Assesment_Name = intent.getStringArrayListExtra("g_ass_name")
        val g_Assesment_Result = intent.getStringArrayListExtra("g_ass_result")
        val myListAdapter = MyListAdapter(this, g_Assesment_Name, g_Assesment_Result)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }
    }


}