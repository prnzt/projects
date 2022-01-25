package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartscholar.MainActivity
import com.example.smartscholar.R;
import com.example.smartscholar.sqlite;
/*Created By Divyanshu Gupta
This Activity is Created for Displaying Sub concepts
 */

class SubConcept : AppCompatActivity() {
    var l_course_ID:String?=null
    var l_concept_ID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_concept)
        l_concept_ID = intent.getStringExtra("CN_ID")
         l_course_ID = intent.getStringExtra("CO_ID")

        var listView = findViewById<ListView>(R.id.listview)
        var db = sqlite(this)
        var l_subConceptArray: ArrayList<String>
        //displaySubConcpet() return array of subconcepts list
        l_subConceptArray = db.displaySubConcept(Integer.parseInt(l_concept_ID!!))
        Toast.makeText(this@SubConcept, l_concept_ID+l_course_ID, Toast.LENGTH_LONG).show()

        var l_arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, l_subConceptArray)
        listView.adapter = l_arrayAdapter
        listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->
            var l_subconcept_Id = db.getsubConceptID(l_subConceptArray.get(position))
            intent = Intent(this, VIewContent::class.java)
            intent.putExtra("CN_ID",l_concept_ID)
            intent.putExtra("CO_ID",l_course_ID)
            intent.putExtra("SC_ID",l_subconcept_Id)
            startActivity(intent)
            Toast.makeText(this@SubConcept, l_concept_ID+l_course_ID+l_subconcept_Id, Toast.LENGTH_LONG).show()

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