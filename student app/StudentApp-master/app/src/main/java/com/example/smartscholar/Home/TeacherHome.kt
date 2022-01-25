package com.example.smartscholar

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqllite.Course
import com.example.sqllite.StudentSelectCohort
import com.example.sqllite.SynchronizeDataCohortSelect


/*Created  By Divyanshu Gupta
This is the TeacherHome  activity it is loaded just after the Login
In this teacher can perform four fucntion that are
1) Performing Classroom session
2) Syncing data directly from cloud to local database
3) Mangaing curricullum
4) Viewing Student Performance
 */

class TeacherHome : AppCompatActivity() {
    var g_mydb1: sqlite? = null
    var g_Username: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        g_mydb1 = sqlite(this)
         g_Username = intent.getStringExtra("g_username")
        Toast.makeText(this@TeacherHome, g_Username!!.toString(), Toast.LENGTH_LONG).show()


    }

    //onclick function for starting classroom session1 and it will open the home  activity where user will enter the cohort and course
    fun classroom_session(view: View?)
    {
        val intent = Intent(this@TeacherHome, Server::class.java)
        startActivity(intent)
    }

    //onclick function for syncing data from  cloud to local database
    fun sync_data(view: View?)
    {
        val intent = Intent(this@TeacherHome, SynchronizeDataCohortSelect::class.java)
        startActivity(intent)
    }

    //onclick function for viewing and managing curicullm
    fun manage_curriculum(view: View?)
    {
        val intent = Intent(this@TeacherHome, Course::class.java)
        startActivity(intent)
    }

    //onclick function for viewing student performance
    fun student_performance(view: View?)
    {
        val intent = Intent(this, StudentSelectCohort::class.java)
        intent.putExtra("g_username",g_Username)

        startActivity(intent)
    }

    fun test_exam(view: View?){
        val intent = packageManager.getLaunchIntentForPackage("com.example.immadisairaj.quiz")
        startActivity(intent)

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
        val intent = Intent(this@TeacherHome, MainActivity::class.java)
        startActivity(intent)

    }

    //prevent from going to previous activity
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}