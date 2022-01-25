package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.smartscholar.MainActivity
import com.example.smartscholar.R
import com.example.smartscholar.sqlite
import java.util.ArrayList

/*
Created by Divyanshu Gupta
In this we are getting cohortand course  for which we have to sync the data
 */
class SynchronizeDataCohortSelect : AppCompatActivity() {

    var g_course_id: String?= null
    var g_course_selected: String?= null
    var g_allCohorts: Spinner? = null
    var g_allCourse: Spinner? = null

    var g_cohortss: List<String> = ArrayList()        //list to be set to spinner
    var g_course: List<String> = ArrayList()           //list to be set to spinner
    var g_adapter: ArrayAdapter<String>? = null
    var db: sqlite? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synchronize_data_cohort_select)
        g_allCohorts = findViewById<View>(R.id.spinner_cohorts) as Spinner
        g_allCourse = findViewById<View>(R.id.spinner_course) as Spinner
        db = sqlite(this)

        //showing cohort using spinner
        prepareCohort()
        //showing courses using spinner
        prepareCourse()

    }

    //prepare  data for spinner(cohort) setting values to spinner
    fun prepareCohort()
    {
        g_cohortss = db!!.getAllCohorts()
        //adapter for spinner
        g_adapter = ArrayAdapter(this@SynchronizeDataCohortSelect, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, g_cohortss)
        //attach adapter to spinner
        g_allCohorts!!.adapter = g_adapter

        //handle click of spinner item
        g_allCohorts!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // clicked item will be shown as spinner
                Toast.makeText(applicationContext, "" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    //prepare  data for spinner(course) setting values to spinner
    fun prepareCourse()
    {
        g_course = db!!.getAllCourse()
        //adapter for spinner
        g_adapter = ArrayAdapter(this@SynchronizeDataCohortSelect, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, g_course)
        //attach adapter to spinner
        g_allCourse!!.adapter = g_adapter

        //handle click of spinner item
        g_allCourse!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // clicked item will be shown as spinner
                Toast.makeText(applicationContext, "" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                g_course_selected= parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }
    fun Next(view: View?)
    {
        fetch_courseid()
        val intent = Intent(this@SynchronizeDataCohortSelect, SynchronizeData::class.java)
        intent.putExtra("co_id",g_course_id)
        startActivity(intent)
    }

    fun fetch_courseid()//function for fetching course id from the local database as we have synced the session previously
    {
        val cursor1: Cursor = db!!.getCourseId(g_course_selected!!)
        val stringBuilder1 = StringBuilder()
        while (cursor1.moveToNext())
        {
            stringBuilder1.append(" " + cursor1.getString(0))
            g_course_id = stringBuilder1.toString()
        }
    }


    //creating option menu for logout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val l_id = item.itemId
        if (l_id == R.id.action_logout) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //onclick for logging out from the teacher's account
    private fun logout()
    {
        (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()// for clearing app data
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

}