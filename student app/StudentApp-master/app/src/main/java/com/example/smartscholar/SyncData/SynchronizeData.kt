package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smartscholar.MainActivity
import com.example.smartscholar.MySingleton
import com.example.smartscholar.R
import com.example.smartscholar.sqlite
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/*Created  By Divyanshu Gupta
This is the Synchronize Data activity for synchronizing data from server to local database according to course and cohort selected
 */

class SynchronizeData : AppCompatActivity() {
    var mydb1: sqlite? = null
    var g_syncstatus:TextView? = null
    var g_syncstatus2:TextView? = null
    var g_syncstatus3:TextView? = null

    val g_JSON_ARRAY = "result"
    var g_course_id: String?= null
    var check:Boolean?=null
    private var preferences: SharedPreferences?=null
    private val PREFS_NAME = "File"
    private val PREFS_NAME2 = "SYNC_STATUS"
    private var preferences2: SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mydb1 = sqlite(this)
        setContentView(R.layout.activity_synchronize_data)
        g_syncstatus = findViewById<View>(R.id.sync1) as TextView?
        g_syncstatus2 = findViewById<View>(R.id.sync2) as TextView?
        g_syncstatus3 = findViewById<View>(R.id.sync3) as TextView?

        val intent = intent
        g_course_id = intent.getStringExtra("co_id")
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences2 = getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE)

        var sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        check = sp.getBoolean("content_sync",false)
        checkSyncstatus()
    }
    private fun checkSyncstatus() {
        var sp = getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE)




        var check:Boolean = sp.getBoolean("sync1",false)
        var check2:Boolean = sp.getBoolean("sync2",false)
        var check3:Boolean = sp.getBoolean("sync3",false)
        if(check==true){
            g_syncstatus!!.setText("Synced")
        }
        else if(check==false){
        g_syncstatus!!.setText("Not Synced")
        }

        if(check2==true){
            g_syncstatus2!!.setText("Synced")
        }
        else if(check2==false){
            g_syncstatus2!!.setText("Not Synced")
        }
        if(check3==true){
            g_syncstatus3!!.setText("Synced")
        }
        else if(check3==false){
            g_syncstatus3!!.setText("Not Synced")
        }
    }
    //onclick function for syncing the session
    fun sync_session_1(view: View?)
    {


        if(check==true) {
            getCurriculumDetail()
            getSessionPlans()
            getConcepts()


        }
     else if(check==false){
            getCurriculumDetail()
            getSessionPlans()
            getSessionSection()
            getConcepts()
            getContent()
            getCourseContent()
            val editor = preferences!!.edit()
            editor.putBoolean("content_sync", true)

            editor.apply()

            g_syncstatus?.setText("Synced")
            val editor2 = preferences2!!.edit()
            editor2.putBoolean("sync1", true)
            editor2.apply()
        }


    }



    fun sync_session_2(view: View?) {

        if(check==true) {
            getCurriculumDetail()
            getSessionPlans()
            getConcepts()


        }
        else if(check==false){
            getCurriculumDetail()
            getSessionPlans()
            getSessionSection()
            getConcepts()
            getContent()
            getCourseContent()
            val editor = preferences!!.edit()
            editor.putBoolean("content_sync", true)

            editor.apply()

            g_syncstatus2?.setText("Synced")
            val editor2 = preferences2!!.edit()
            editor2.putBoolean("sync2", true)
            editor2.apply()
        }

    }
    fun sync_session_3(view: View?) {

        if(check==true) {
            getCurriculumDetail()
            getSessionPlans()
            getConcepts()


        }
        else if(check==false){
            getCurriculumDetail()
            getSessionPlans()
            getSessionSection()
            getConcepts()
            getContent()
            getCourseContent()
            val editor = preferences!!.edit()
            editor.putBoolean("content_sync", true)

            editor.apply()

            g_syncstatus3?.setText("Synced")
            val editor2 = preferences2!!.edit()
            editor2.putBoolean("sync3", true)
            editor2.apply()
        }

    }

    fun getCurriculumDetail(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_CURRICULUMDETAILS, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val Response = jsonObject.getJSONArray("response")

                val l_CD_CU_ID = "CU_id"
                val l_CD_CO_ID = "CO_id"
                val l_CD_CO_SEQNO = "CO_SeqNo"
                val l_CD_CO_SEMESTER = "CO_Semester"
                val l_CD_CO_YEAR = "CO_Year"


                    var l_CU_id:Int?=null
                    var l_CO_id:Int?=null
                    var l_CO_SeqNo:Int?=null
                    var l_CO_Semester:Int?=null
                    var l_CO_Year:Int?=null


                    for (i in 0 until Response.length())
                    {
                        val jo = Response.getJSONObject(i)
                        l_CU_id=jo.getInt(l_CD_CU_ID)
                        l_CO_id=jo.getInt(l_CD_CO_ID)
                        l_CO_SeqNo=jo.getInt(l_CD_CO_SEQNO)
                        l_CO_Semester=jo.getInt(l_CD_CO_SEMESTER)
                        l_CO_Year=jo.getInt(l_CD_CO_YEAR)

                        mydb1?.insertData_into_CurriculumDetails(l_CU_id,l_CO_id,l_CO_SeqNo,l_CO_Semester,l_CO_Year)
                        getCurriculum(l_CU_id)
                    }
            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@SynchronizeData, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //sending the user email and password for verification to the php file
                params["co_id"] = g_course_id!!
                return params
            }
        }
        MySingleton.getInstance(this@SynchronizeData)!!.addToRequestQue(stringRequest)
    }

    fun getCurriculum(cu_id: Int?){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_CURRICULUM, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val Response = jsonObject.getJSONArray("response")

                val l_CU_ID = "CU_id"
                val l_CU_NAME = "CU_Name"
                val l_CU_DESC = "CU_Desc"
                val l_CU_IMAGE = "CU_Image"
                val l_CU_INSERTDATE = "CU_Insertdate"

                var l_CU_id: Int? = null
                var l_CU_Name: String? = null
                var l_CU_Desc: String? = null
                var l_CU_Image: String? = null
                var l_CU_InsertDate: String? = null


                for (i in 0 until Response.length()) {
                    val jo = Response.getJSONObject(i)

                    l_CU_id = jo.getInt(l_CU_ID)
                    l_CU_Name = jo.getString(l_CU_NAME)
                    l_CU_Desc = jo.getString(l_CU_DESC)
                    l_CU_Image = jo.getString(l_CU_IMAGE)
                    l_CU_InsertDate = jo.getString(l_CU_INSERTDATE)

                    mydb1?.insertData_into_Curriculum(l_CU_id, l_CU_Name, l_CU_Desc, l_CU_Image, l_CU_InsertDate)

                }
            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@SynchronizeData, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //sending the user email and password for verification to the php file
                params["cu_id"] = cu_id.toString()
                return params
            }
        }
        MySingleton.getInstance(this@SynchronizeData)!!.addToRequestQue(stringRequest)
    }



    fun getContent() {

        val url: String = SERVER_URL_CONTENT
        val stringRequest = StringRequest(url,object : Response.Listener<String?> {


            override fun onResponse(response: String?) {
                response?.let { showJSON1(it) }
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(this@SynchronizeData, error.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun showJSON1(response: String) {
        val l_CT_ID = "CT_id"
        val l_CT_NAME= "CT_Name"
        val l_CT_TYPE = "CT_Type"
        val l_CT_CONTENTLINK= "CT_ContentLink"
        val l_CT_DURATION = "CT_Duration"
        val l_CT_INSERTDATE= "CT_InsertDate"



    try {
        val jsonObject = JSONObject(response)
        val result = jsonObject.getJSONArray(g_JSON_ARRAY)
        var l_CT_id: Int? = null
        var l_CT_Name: String? = null
        var l_CT_Type: String? = null
        var l_CT_ContentLink: String? = null
        var l_CT_Duration: Int? = null
        var l_CT_InsertDate: String? = null


        for (i in 0 until result.length()) {
            val jo = result.getJSONObject(i)
            l_CT_id = jo.getInt(l_CT_ID)
            l_CT_Name = jo.getString(l_CT_NAME)
            l_CT_Type = jo.getString(l_CT_TYPE)
            l_CT_ContentLink = jo.getString(l_CT_CONTENTLINK)
            l_CT_Duration = jo.getInt(l_CT_DURATION)
            l_CT_InsertDate = jo.getString(l_CT_INSERTDATE)

            mydb1?.insertData_into_Content(l_CT_id, l_CT_Name, l_CT_Type, l_CT_ContentLink, l_CT_Duration, l_CT_InsertDate)


        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

        }




    fun getConcepts(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_CONCEPT, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val Response = jsonObject.getJSONArray("response")

                val l_CN_ID = "CN_id"
                val l_CN_NAME= "CN_Name"
                val l_CN_DESC = "CN_Desc"
                val l_CN_DURATION = "CN_Duration"
                val l_CN_IMAGE = "CN_Image"
                val l_CN_INSERTDATE= "CN_Insertdate"
                val l_CO_CN_ID="CO_id"

                var l_CN_id:Int
                var l_CN_Name:String?=null
                var l_CN_Desc:String?=null
                var l_CN_Duration:Int?=null
                var l_CN_Image:String?=null
                var l_CN_Insertdate:String?=null
                var l_CO_CN_id:Int?=null

                for (i in 0 until Response.length()) {
                    val jo = Response.getJSONObject(i)
                    l_CN_id=jo.getInt(l_CN_ID)
                    l_CN_Name=jo.getString(l_CN_NAME)
                    l_CN_Desc=jo.getString(l_CN_DESC)
                    l_CN_Duration=jo.getInt(l_CN_DURATION)
                    l_CN_Image=jo.getString(l_CN_IMAGE)
                    l_CN_Insertdate=jo.getString(l_CN_INSERTDATE)
                    l_CO_CN_id=jo.getInt(l_CO_CN_ID)

                    mydb1?.insertData_into_Concept(l_CN_id,l_CN_Name,l_CN_Desc,l_CN_Duration,l_CN_Image,l_CN_Insertdate,l_CO_CN_id)
                    getSubConcepts(l_CN_ID)
                }

            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@SynchronizeData, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //sending the user email and password for verification to the php file
                params["co_id"] = g_course_id!!
                return params
            }
        }
        MySingleton.getInstance(this@SynchronizeData)!!.addToRequestQue(stringRequest)
    }

    fun getSubConcepts(cn_id: String){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_SUBCONCEPT, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val Response = jsonObject.getJSONArray("response")

                val l_SC_ID = "SC_id"
                val l_SC_NAME= "SC_Name"
                val l_SC_DESC = "SC_Desc"
                val l_SC_INSERTDATE= "SC_Insertdate"
                val l_SC_DURATION = "SC_Duration"
                val l_CN_SC_ID="CN_id"
                val l_CO_SC_ID="CO_id"

                var l_SC_id:Int?=null
                var l_SC_Name:String?=null
                var l_SC_Desc:String?=null
                var l_SC_Insertdate:String?=null
                var l_SC_Duration:Int?=null
                var l_CN_SC_id:Int?=null
                var l_CO_SC_id:Int?=null

                for (i in 0 until Response.length()) {
                    val jo = Response.getJSONObject(i)
                    l_SC_id=jo.getInt(l_SC_ID)
                    l_SC_Name=jo.getString(l_SC_NAME)
                    l_SC_Desc=jo.getString(l_SC_DESC)
                    l_SC_Insertdate=jo.getString(l_SC_INSERTDATE)
                    l_SC_Duration=jo.getInt(l_SC_DURATION)
                    l_CN_SC_id=jo.getInt(l_CN_SC_ID)

                    l_CO_SC_id=jo.getInt(l_CO_SC_ID)


                    mydb1?.insertData_into_SubConcept(l_SC_id,l_SC_Name,l_SC_Desc,l_SC_Insertdate,l_SC_Duration,l_CN_SC_id,l_CO_SC_id)
                }

            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@SynchronizeData, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //sending the user email and password for verification to the php file
                params["co_id"] = g_course_id!!
                params["cn_id"] = cn_id

                return params
            }
        }
        MySingleton.getInstance(this@SynchronizeData)!!.addToRequestQue(stringRequest)
    }


    fun getSessionPlans(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, SERVER_URL_SESSIONPLAN, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val Response = jsonObject.getJSONArray("response")

                val l_SP_ID = "SP_id"
                val l_SP_NAME= "SP_Name"
                val l_SP_DURATION = "SP_Duration"
                val l_SP_SEQUENCE= "SP_Sequence"
                val l_CO_SP_ID = "CO_id"


                var l_SP_id:Int?=null
                var l_SP_Name:String?= null
                var l_SP_Duration:Int?=null
                var l_SP_Sequence:Int?=null
                var l_CO_SP_id:Int?=null

                for (i in 0 until Response.length()) {
                    val jo = Response.getJSONObject(i)
                    l_SP_id=jo.getInt(l_SP_ID)
                    l_SP_Name=jo.getString(l_SP_NAME)
                    l_SP_Duration=jo.getInt(l_SP_DURATION)
                    l_SP_Sequence=jo.getInt(l_SP_SEQUENCE)
                    l_CO_SP_id=jo.getInt(l_CO_SP_ID)

                    mydb1?.insertData_into_SessionPlan(l_SP_id,l_SP_Name,l_SP_Duration,l_SP_Sequence,l_CO_SP_id)
                }

            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(this@SynchronizeData, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //sending the user email and password for verification to the php file
                params["co_id"] = g_course_id!!
                return params
            }
        }
        MySingleton.getInstance(this@SynchronizeData)!!.addToRequestQue(stringRequest)
    }


    fun getSessionSection() {

        val url: String = SERVER_URL_SESSIONSECTION
        val stringRequest = StringRequest(url, object : Response.Listener<String?> {


            override fun onResponse(response: String?) {
                response?.let { showJSON4(it) }
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(this@SynchronizeData, error.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun showJSON4(response: String) {
        val l_SS_ID = "SS_id"
        val l_SS_CONTENT= "SS_Content"
        val l_SS_CONTENTTYPE = "SS_ContentType"
        val l_SS_SEQNUM= "SS_Seqnum"
        val l_SS_DURATION = "SS_Duration"
        val l_SP_SS_ID="SP_id"
        val l_SC_SS_ID="SC_id"
        val l_CO_SS_ID="CO_id"
        val l_CT_SS_ID="CT_id"

        try {
            val jsonObject = JSONObject(response)
            val result = jsonObject.getJSONArray(g_JSON_ARRAY)
            var l_SS_id:Int?=null
            var l_SS_Content:String?= null
            var l_SS_ContentType:String?=null
            var l_SS_Seqnum:Int?=null
            var l_SS_Duration:Int?=null
            var l_SP_SS_id:Int?=null
            var l_SC_SS_id:Int?=null
            var l_CO_SS_id:Int?=null
            var l_CT_SS_id:Int?=null


            for (i in 0 until result.length()) {
                val jo = result.getJSONObject(i)
                l_SS_id=jo.getInt(l_SS_ID)
                l_SS_Content=jo.getString(l_SS_CONTENT)
                l_SS_ContentType=jo.getString(l_SS_CONTENTTYPE)
                l_SS_Seqnum=jo.getInt(l_SS_SEQNUM)
                l_SS_Duration=jo.getInt(l_SS_DURATION)
                l_SP_SS_id=jo.getInt(l_SP_SS_ID)
                l_SC_SS_id=jo.getInt(l_SC_SS_ID)
                l_CO_SS_id=jo.getInt(l_CO_SS_ID)
                l_CT_SS_id=jo.getInt(l_CT_SS_ID)


                mydb1?.insertData_into_SessionSection(l_SS_id,l_SS_Content,l_SS_ContentType,l_SS_Seqnum,l_SS_Duration,l_SP_SS_id,l_SC_SS_id,l_CO_SS_id,l_CT_SS_id)
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
    }



    fun getCourseContent() {

        val url: String = SERVER_URL_COURSECONTENT
        val stringRequest = StringRequest(url, object : Response.Listener<String?> {


            override fun onResponse(response: String?) {
                response?.let { showJSON7(it) }
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(this@SynchronizeData, error.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun showJSON7(response: String) {

        val l_CO_CC_ID = "CO_id"
        val l_CN_CC_ID = "CN_id"
        val l_SC_CC_ID = "SC_id"
        val l_CT_CC_ID = "CT_id"
        try {
            val jsonObject = JSONObject(response)
            val result = jsonObject.getJSONArray(g_JSON_ARRAY)
            var l_CO_id:Int?=null
            var l_CN_id:Int?=null
            var l_SC_id:Int?=null
            var l_CT_id:Int?=null


            for (i in 0 until result.length())
            {
                val jo = result.getJSONObject(i)
                l_CO_id=jo.getInt(l_CO_CC_ID)
                l_CN_id=jo.getInt(l_CN_CC_ID)
                l_SC_id=jo.getInt(l_SC_CC_ID)
                l_CT_id=jo.getInt(l_CT_CC_ID)

                mydb1?.insertData_into_CourseContent(l_CO_id,l_CN_id,l_SC_id,l_CT_id)

            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }


    companion object
    {

        const val SERVER_URL_CONTENT = "http:/10.0.2.2/poc/getContent.php"
        const val SERVER_URL_CONCEPT = "http:/10.0.2.2/poc/getConcept2.php"
        const val SERVER_URL_SUBCONCEPT = "http:/10.0.2.2/poc/getSubConcept2.php"
        const val SERVER_URL_SESSIONPLAN = "http:/10.0.2.2/poc/getSessionPlan2.php"
        const val SERVER_URL_SESSIONSECTION = "http:/10.0.2.2/poc/getSessionSection.php"

        const val SERVER_URL_COURSECONTENT = "http:/10.0.2.2/poc/getCourseContent.php"
        const val SERVER_URL_CURRICULUM = "http:/10.0.2.2/poc/getCurriculum2.php"
        const val SERVER_URL_CURRICULUMDETAILS = "http:/10.0.2.2/poc/getCurriculumDetails2.php"





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