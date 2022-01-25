package com.example.smartscholar

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartscholar.MainActivity
import com.example.smartscholar.R

/*Created  By Divyanshu Gupta
This is the reset password activity for aking input of the user email id
 */
class ResetPasswordActivity : AppCompatActivity() {
    var g_edEmail: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        g_edEmail = findViewById(R.id.edEmail)
    }

    //onclick function for resetting password
    fun reset_btn(view: View?)
    {
        if (checkNetworkConnection()) //checking whether app is connected to internet or not(if connected it will return true and hence the code below will run)
        {
            val l_email_user1 = g_edEmail?.getText().toString()
            if (l_email_user1.isEmpty()) //checking wether email id is written in text box or not
            {
                g_edEmail!!.error = "Email is required!"
            }
            else
            {
                val intent = Intent(this@ResetPasswordActivity, ResetPasswordActivity2::class.java)
                    intent.putExtra("email_user1", l_email_user1)//sending the email id to ResetPasswordActivity2 for changing the password
                    startActivity(intent)
                }
            }
        else {
            Toast.makeText(this@ResetPasswordActivity, "Connect to Internet & try again", Toast.LENGTH_LONG).show()
        }
    }



    //functon for checking whether the app is connected to internet or not
    fun checkNetworkConnection(): Boolean
    {
        val l_connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val l_networkInfo = l_connectivityManager.activeNetworkInfo
        return l_networkInfo != null && l_networkInfo.isConnected
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