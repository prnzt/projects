package com.example.smartscholar



import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONException
import org.json.JSONObject
import java.util.*


/*Created  By Divyanshu Gupta
This is the main activity and hence the login activity of the app too

In this we are also syncing cohort and course from server into the local database
 */
class MainActivity : AppCompatActivity() {
    var g_email: EditText? = null
    var g_password: EditText? = null
    var g_remember:CheckBox?=null
    var mydb1: sqlite? = null

    val g_JSON_ARRAY = "result"
    private val PREFS_NAME = "PrefsFile"
    private val PREFS_NAME2 = "CohortSync"

    private var preferences:SharedPreferences?=null
    private var preferences2:SharedPreferences?=null
    var g_ST_Id:Int ? = null
    var g_name_id: MutableMap<String, Int> = mutableMapOf()

    var g_student_name :ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hide actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()

        mydb1 = sqlite(this)
        g_email = findViewById(R.id.email)
        g_password = findViewById(R.id.pass)
        g_remember= findViewById<CheckBox>(R.id.remeberme)
//preferences
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences2 = getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE)
        isReadStoragePermissionGranted()
        getPreferencesData()
    }

    fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 3)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            3 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
            }
        }
    }


    //setting the rememberd username and password
    private fun getPreferencesData() {
        var sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if(sp.contains("pref_username")) {
            var username = sp.getString("pref_username", "not found.")
            g_email!!.setText(username)
        }
        if(sp.contains("pref_password")) {
            var passsword = sp.getString("pref_password", "not found.")
            g_password!!.setText(passsword)
        }
        if(sp.contains("pref_check")) {
            var check:Boolean = sp.getBoolean("pref_check",false)
            g_remember!!.setChecked(check)


        }

        var sp2 = getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE)

        var check:Boolean = sp2.getBoolean("pref_sync_cohort_&_course",false)

        if(check==true){
            Toast.makeText(this,"Welcome User",Toast.LENGTH_SHORT).show()
        }
        else if(check==false){
            getCohortData()
            getCourseData()
        }



    }


    //onclick function for logging in the user and verifing the user details
    fun login_main(view: View?)
    {
        val l_email_user = g_email!!.text.toString()
        val l_password_user = g_password!!.text.toString()
        if (checkNetworkConnection())//checking whether app is connected to internet or not(if connected it will return true and hence the code below will run)
        {
            val stringRequest: StringRequest = object : StringRequest(Method.POST, g_SERVER_URL, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val Response = jsonObject.getString("response")
                    if (Response == "Login Success")//the php code will return the response that is login success if user and password are correct in server database
                    {
                        Toast.makeText(this@MainActivity, "success", Toast.LENGTH_LONG).show()

                        //checking whether remember checkox is checked or not
                        if(g_remember!!.isChecked){

                            val boolIsChecked = g_remember!!.isChecked
                            val editor = preferences!!.edit()
                            editor.putString("pref_username",l_email_user)
                            editor.putString("pref_password",l_password_user)
                            editor.putBoolean("pref_sync_cohort_&_course",true)

                            editor.putBoolean("pref_check",boolIsChecked)
                            editor.apply()
                            Toast.makeText(this@MainActivity,"Setting have been saved",Toast.LENGTH_LONG).show()

                        }
                        else{
                            preferences!!.edit().clear().apply()
                        }

                        Toast.makeText(this@MainActivity, l_email_user, Toast.LENGTH_LONG).show()


                        val intent = Intent(this@MainActivity, TeacherHome::class.java)
                        intent.putExtra("g_username",l_email_user)

                        startActivity(intent)

                        //clearing the text boxes after directing to the other activity
                        g_email!!.text.clear()
                        g_password!!.text.clear()
                    }
                    else
                    {
                        Toast.makeText(this@MainActivity, "Wrong Email id/Password", Toast.LENGTH_LONG).show()
                    }
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { Toast.makeText(this@MainActivity, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //sending the user email and password for verification to the php file
                    params["email"] = l_email_user
                    params["password"] = l_password_user
                    return params
                }
            }
            MySingleton.getInstance(this@MainActivity)!!.addToRequestQue(stringRequest)
        }
        val editor = preferences2!!.edit()
        editor.putBoolean("pref_sync_cohort_&_course",true)
        editor.apply()
    }

    fun getCohortData()
    {
        val url: String = SERVER_URL_COHORT
        val stringRequest = StringRequest(url, object : Response.Listener<String?> {


            override fun onResponse(response: String?) {
                response?.let { showJSON_for_cohort(it) }
            }
        },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(this@MainActivity, error.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun showJSON_for_cohort(response: String) {
        val l_CH_ID = "CH_id"
        val l_CH_NAME = "CH_Name"
        val l_TC_ID = "TC_id"
        val l_CU_ID = "CU_id"
        val l_TP_ID = "TP_id"
        val l_CH_SEMESTER = "CH_Semester"
        try
        {
            val jsonObject = JSONObject(response)
            val result = jsonObject.getJSONArray(g_JSON_ARRAY)
            var l_CH_id:Int?=null
            var l_CH_Name:String?= null
            var l_TC_id:Int?=null
            var l_CU_id:Int?=null
            var l_TP_id:Int?=null
            var l_CH_Semester:Int?= null

            for (i in 0 until result.length()) {
                val jo = result.getJSONObject(i)
                l_CH_id=jo.getInt(l_CH_ID)
                l_CH_Name=jo.getString(l_CH_NAME)
                l_TC_id=jo.getInt(l_TC_ID)
                l_CU_id=jo.getInt(l_CU_ID)
                l_TP_id=jo.getInt(l_TP_ID)
                l_CH_Semester=jo.getInt(l_CH_SEMESTER)

                mydb1?.insertData_into_Cohort(l_CH_id,l_CH_Name,l_TC_id,l_CU_id,l_TP_id,l_CH_Semester)
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }


    fun getCourseData() {

        val url: String = SERVER_URL_COURSE
        val stringRequest = StringRequest(url, object : Response.Listener<String?> {


            override fun onResponse(response: String?) {
                response?.let { showJSON_for_course(it) }
            }
        },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(this@MainActivity, error.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun showJSON_for_course(response: String) {
        val l_CO_ID = "CO_id"
        val l_CO_NAME = "CO_Name"
        val l_CO_DESC = "CO_Desc"
        val l_CO_DURATION = "CO_Duration"
        val l_CO_IMAGE = "CO_Image"
        val l_CO_INSERTDATE = "CO_Insertdate"
        try {
            val jsonObject = JSONObject(response)
            val result = jsonObject.getJSONArray(g_JSON_ARRAY)

            var l_CO_id: Int? = null
            var l_CO_Name: String? = null
            var l_CO_Desc: String? = null
            var l_CO_Duration: Int? = null
            var l_CO_Image: String? = null
            var l_CO_InsertDate: String? = null


            for (i in 0 until result.length()) {
                val jo = result.getJSONObject(i)

                l_CO_id = jo.getInt(l_CO_ID)
                l_CO_Name = jo.getString(l_CO_NAME)
                l_CO_Desc = jo.getString(l_CO_DESC)
                l_CO_Duration = jo.getInt(l_CO_DURATION)
                l_CO_Image = jo.getString(l_CO_IMAGE)
                l_CO_InsertDate = jo.getString(l_CO_INSERTDATE)

                mydb1?.insertData_into_Course(l_CO_id, l_CO_Name, l_CO_Desc, l_CO_Duration, l_CO_Image, l_CO_InsertDate)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }





    //onclick function for resetting the password this function takes you to the reset password activity
    fun forget(view: View?)
    {
        if (checkNetworkConnection()) {
            val intent = Intent(this@MainActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity, "Connect to Internet & try again", Toast.LENGTH_LONG).show()
        }
    }

    //functon for checking whether the app is connected to internet or not
    fun checkNetworkConnection(): Boolean
    {
        val l_connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val l_networkInfo = l_connectivityManager.activeNetworkInfo
        return l_networkInfo != null && l_networkInfo.isConnected
    }



    companion object {

        const val g_SERVER_URL = "http:/10.0.2.2/poc/studentlogin.php"
        const val SERVER_URL_COHORT = "http:/10.0.2.2/poc/getCohort.php"
        const val SERVER_URL_COURSE = "http:/10.0.2.2/poc/getCourse.php"

    }

}


