package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.smartscholar.MySingleton
import com.example.smartscholar.R;

import com.example.sqllite.StudentPerformance.MyListAdapter
import kotlinx.android.synthetic.main.activity_student_info.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/*
Created by Amrit and Divyanshu
 */

class StudentInfo : AppCompatActivity() {
    var g_student_name: TextView? = null
    var g_student_batch: TextView? = null
    var g_student_course: TextView? = null
    var g_student_dob: TextView? = null
    var g_student_add: TextView? = null
    var g_student_id: TextView? = null
    var l_ST_Id: Int? = null
    var l_studentID: Int?=null
    var g_ST_id:String?=null
var g_name:String?=null
    var g_Assesment_Name:ArrayList<String> = arrayListOf()
    var g_Assesment_Result:ArrayList<String> = arrayListOf()
    var g_Username: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_info)

        //hide actionbar
        g_Username = intent.getStringExtra("g_username")
        Toast.makeText(this@StudentInfo, g_Username!!.toString(), Toast.LENGTH_LONG).show()

        //hide actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()
        g_student_name = findViewById(R.id.name_textView)
        g_student_batch = findViewById(R.id.batch_textview)
        g_student_course = findViewById(R.id.course_textview)
        g_student_dob = findViewById(R.id.dob_textView)
        g_student_add = findViewById(R.id.add_textview)
        g_student_id = findViewById(R.id.studentId_textView)
        fetchDetails()
        fetchResult()
    }
    fun view_grade(view: View?){

        val intent = Intent(this@StudentInfo, Grades::class.java)
        intent.putExtra("g_name",g_name)
        intent.putStringArrayListExtra("g_ass_name",g_Assesment_Name)
        intent.putStringArrayListExtra("g_ass_result",g_Assesment_Result)

        startActivity(intent)

    }

//fetchDetails() function fetch details of student from server__
    fun fetchDetails() {


    var SERVER_URL_STUDENT = "http://10.0.2.2/poc/getIndividualStudentDetails.php"

        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_STUDENT, Response.Listener { response ->
            try {

                val l_ST_ID = "ST_id"
                val l_ST_FIRSTNAME = "ST_Firstname"
                val l_ST_LASTNAME = "ST_Lastname"
                val l_ST_ADDRESS = "ST_Address"
                val l_ST_DOB = "ST_Dob"


                val jsonObject = JSONObject(response)
                val result = jsonObject.getJSONArray("result")

                var l_ST_Firstname: String? = null
                var l_ST_Lastname: String? = null
                var l_ST_Address: String? = null
                var l_ST_Dob: String? = null

                for (i in 0 until result.length()) {
                    val jo = result.getJSONObject(i)
                    l_ST_Id = jo.getInt(l_ST_ID)

                    l_ST_Firstname = jo.getString(l_ST_FIRSTNAME)
                    l_ST_Lastname = jo.getString(l_ST_LASTNAME)
                    l_ST_Address = jo.getString(l_ST_ADDRESS)
                    l_ST_Dob = jo.getString(l_ST_DOB)

                    var name: String = l_ST_Firstname + " " + l_ST_Lastname
                    g_name=name
                    g_student_name?.setText(name)
                    g_student_add?.setText(l_ST_Address)
                    g_student_dob?.setText(l_ST_Dob)
                    g_student_id?.setText(l_ST_Id.toString())
                    g_ST_id = l_ST_Id.toString()


                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@StudentInfo, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                //sending the Student ID for fetching student details from server
                params["Username"] = g_Username.toString()

                return params
            }
        }
        MySingleton.getInstance(this@StudentInfo)!!.addToRequestQue(stringRequest)
    }



    fun fetchResult() {


      val SERVER_URL_RESULT = "http://10.0.2.2/poc/getStudentResult.php"

        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_RESULT, Response.Listener { response ->
            try {

                val l_AM_NAME = "AM_Name"
                val l_AR_RESULT = "AR_Result"


                val jsonObject = JSONObject(response)
                val result = jsonObject.getJSONArray("result")
                var l_AM_Name: String? = null
                var l_AR_Result:Int?=null

                for (i in 0 until result.length()) {
                    val jo = result.getJSONObject(i)
                    l_AM_Name = jo.getString(l_AM_NAME)
                    l_AR_Result = jo.getInt(l_AR_RESULT)
                    g_Assesment_Name.add(l_AM_Name)
                    g_Assesment_Result.add(l_AR_Result.toString())


                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@StudentInfo, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String>? {
                val params: MutableMap<String, String> = HashMap()

                //sending the Student ID, Course id, Cohort id for fetching student result details from server
                params["ST_ID"] = "9"
                // params["CO_ID"] ="1"
               // params["CH_ID"] ="1"

                return params
            }
        }
        MySingleton.getInstance(this@StudentInfo)!!.addToRequestQue(stringRequest)
    }




}