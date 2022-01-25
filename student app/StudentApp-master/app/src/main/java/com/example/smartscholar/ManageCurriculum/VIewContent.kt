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
/*
Created by Divyanshu gupta
In this we are displaying all the content
 */
class VIewContent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_iew_content)


        var listView = findViewById<ListView>(R.id.listview)
        var db = sqlite(this)
        val l_concept_ID = intent.getStringExtra("CN_ID")
        val l_course_ID = intent.getStringExtra("CO_ID")
        val l_subconcept_ID = intent.getStringExtra("SC_ID")

        var l_content: ArrayList<String>
        l_content = db.displayContent(l_course_ID,l_concept_ID,l_subconcept_ID)

        var l_arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, l_content)
        listView.adapter = l_arrayAdapter
        listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->
            var l_contentlink = l_content.get(position)
            var l_ct_id: String? = db.getContentId(l_content.get(position))
            intent = Intent(this, DownloadActivity::class.java)
            intent.putExtra("CT_LINK",l_contentlink)
            intent.putExtra("CT_ID",l_ct_id)
            startActivity(intent)
            Toast.makeText(this@VIewContent, l_contentlink, Toast.LENGTH_LONG).show()

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