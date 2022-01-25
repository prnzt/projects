package com.example.sqllite

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.smartscholar.MainActivity
import com.example.smartscholar.sqlite
import com.example.smartscholar.R;

/*Created By Amrit Kumar
This Activity is Created for Displaying Concepts
 */
class ConceptActivity : AppCompatActivity() {
    var l_CO_ID:String?=null
    lateinit var textView: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.smartscholar.R.layout.activity_concept)
        textView = findViewById<TextView>(com.example.smartscholar.R.id.textView)
        openConcept();
    }


    fun openConcept() {
         l_CO_ID = intent.getStringExtra("COURSE_ID")
        var listView = findViewById<ListView>(com.example.smartscholar.R.id.listview)
        var db = sqlite(this)
        var l_concept_array: ArrayList<String>
        //displayConcpet() return array of concept list
        l_concept_array = db.displayConcept(Integer.parseInt(l_CO_ID!!))

        var l_arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, l_concept_array)

        listView.adapter = l_arrayAdapter

        listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->
            var l_concept_Id = db.getConceptID(l_concept_array.get(position))
            intent = Intent(this, SubConcept::class.java)
            intent.putExtra("CN_ID",l_concept_Id)
            intent.putExtra("CO_ID",l_CO_ID)
            startActivity(intent)
            Toast.makeText(this@ConceptActivity, "coid="+l_CO_ID, Toast.LENGTH_LONG).show()


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