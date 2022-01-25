package com.example.smartscholar

import android.R
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.smartscholar.MainActivity
import com.example.smartscholar.sqlite
import java.util.*

/*Created  By Divyanshu Gupta
This is the Home  activity for starting the session according to the cohort  and course selected
 */
class Home : AppCompatActivity() {

    var g_course_id: String?= null
    var g_sessionplan_id: String?= null
    var g_sessionplan_name: String?= null
    var g_concept_id: String?= null
    var g_curriculum_id: String?= null

    var g_subconcept_id: String?= null

    var g_course_selected: String?= null
    var g_allCohorts: Spinner? = null
    var g_allCourse: Spinner? = null

    var g_cohortss: List<String> = ArrayList()        //list to be set to spinner
    var g_course: List<String> = ArrayList()           //list to be set to spinner
    var g_adapter: ArrayAdapter<String>? = null
    var db: sqlite? = null

    override fun onCreate(savedInstanceState: Bundle?)
    { super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
    //    g_allCohorts = findViewById<View>(R.id.spinner_cohorts) as Spinner
    //    g_allCourse = findViewById<View>(R.id.spinner_course) as Spinner
        db = sqlite(this)


        //showing cohort using spinner
        prepareCohort()
        //showing courses using spinner
        prepareCourse()
    }//end of oncreate function

    //prepare  data for spinner(cohort) setting values to spinner
    fun prepareCohort()
    {
        g_cohortss = db!!.getAllCohorts()
        //adapter for spinner
        g_adapter = ArrayAdapter(this@Home, R.layout.simple_spinner_dropdown_item, android.R.id.text1, g_cohortss)
        //attach adapter to spinner
        g_allCohorts!!.adapter = g_adapter

        //handle click of spinner item
        g_allCohorts!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // clicked item will be shown as spinner
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    //prepare  data for spinner(course) setting values to spinner
    fun prepareCourse()
    {
        g_course = db!!.getAllCourse()
        //adapter for spinner
        g_adapter = ArrayAdapter(this@Home, R.layout.simple_spinner_dropdown_item, android.R.id.text1, g_course)
        //attach adapter to spinner
        g_allCourse!!.adapter = g_adapter

        //handle click of spinner item
        g_allCourse!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // clicked item will be shown as spinner
                g_course_selected= parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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

    fun fetch_sessionid()//function for fetching session id from the local database as we have synced the session previously
    {
        val cursor2: Cursor = db!!.getSessionId(g_course_id!!)
        val stringBuilder2 = StringBuilder()
        while (cursor2.moveToNext())
        {
            stringBuilder2.append(" " + cursor2.getString(0))
            g_sessionplan_id = stringBuilder2.toString()
            g_sessionplan_name = cursor2.getString(1)
        }
    }

    fun fetch_conceptid()//function for fetching concept id from the local database as we have synced the session previously
    {
        val cursor3: Cursor = db!!.getConceptId(g_course_id!!)
        val stringBuilder3 = StringBuilder()
        while (cursor3.moveToNext())
        {
            stringBuilder3.append(" " + cursor3.getString(0))
            g_concept_id = stringBuilder3.toString()
        }
    }

    fun fetch_subconceptid()//function for fetching subconcept id from the local database as we have synced the session previously
    {
        val cursor4: Cursor = db!!.getSubConceptId(g_concept_id!!,g_course_id!!)
        val stringBuilder4 = StringBuilder()
        while (cursor4.moveToNext())
        {
            stringBuilder4.append(" " + cursor4.getString(0))
            g_subconcept_id = stringBuilder4.toString()
        }
    }

    fun fetch_curriculumid()//function for fetching subconcept id from the local database as we have synced the session previously
    {
        val cursor4: Cursor = db!!.getCurriculumId(g_course_id!!)
        val stringBuilder4 = StringBuilder()
        while (cursor4.moveToNext())
        {
            stringBuilder4.append(" " + cursor4.getString(0))
            g_curriculum_id = stringBuilder4.toString()
        }
    }


    //onClick Function for starting the session
    fun start_session(view: View?)
    {
        fetch_courseid()
        fetch_sessionid()
        fetch_conceptid()
        fetch_subconceptid()
        fetch_curriculumid()

       // val intent = Intent(this@Home, SessionSelect::class.java)
        intent.putExtra("cn_id",g_concept_id)
        intent.putExtra("sc_id",g_subconcept_id)
        intent.putExtra("co_id",g_course_id)
        intent.putExtra("sp_name",g_sessionplan_name)
        intent.putExtra("sp_id",g_sessionplan_id)
        intent.putExtra("cu_id",g_curriculum_id)

        startActivity(intent)
    }

    //creating option menu for logout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     //   menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

  //  override fun onOptionsItemSelected(item: MenuItem): Boolean {
     //   val l_id = item.itemId
       // if (l_id == R.id.action_logout) {
      //      logout()
    //        return true
    //    }
    //    return super.onOptionsItemSelected(item)
   // }

    //onclick for logging out from the teacher's account
    private fun logout()
    {
        (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()// for clearing app data
        val intent = Intent(this@Home, MainActivity::class.java)
        startActivity(intent)
    }

 
}

//end of class