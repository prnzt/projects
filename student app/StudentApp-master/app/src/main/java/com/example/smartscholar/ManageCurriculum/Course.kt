package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartscholar.MainActivity
import com.example.smartscholar.R;
import com.example.smartscholar.sqlite

/*Created By Amrit Kumar
This Activity is Created for Displaying Courses
 */
class Course : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        var listView = findViewById<ListView>(R.id.listview)
        var db = sqlite(this)
        var l_courseArray: ArrayList<String>
        l_courseArray = db.displayCourse()
        var l_arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, l_courseArray)
        listView.adapter = l_arrayAdapter

        listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->

            var l_selectedCourse: String = l_courseArray.get(position)
            var l_course_ID: String? = db.getCourseID(l_selectedCourse)
            intent = Intent(this, ConceptActivity::class.java)
            intent.putExtra("COURSE_ID", l_course_ID)
            startActivity(intent)
            Toast.makeText(this@Course, l_course_ID ,Toast.LENGTH_LONG).show()

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