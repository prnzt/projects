package com.example.smartscholar

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.smartscholar.MainActivity
import com.example.smartscholar.MySingleton
import com.example.smartscholar.R
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/*Created  By Divyanshu Gupta
This is the reset password activity for changing the passsworf of the user directly in the server database
 */
class ResetPasswordActivity2 : AppCompatActivity() {
    var g_new_pass: EditText? = null
    var g_confirm_new_pass: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password2)
        g_new_pass = findViewById(R.id.new_pass)
        g_confirm_new_pass = findViewById(R.id.pass)

    }
    fun reset_btn2(view: View?)
    {
        val l_i1 = intent
        val l_emailuser = l_i1.getStringExtra("email_user1")
        if (checkNetworkConnection())  //checking whether app is connected to internet or not(if connected it will return true and hence the code below will run)
        {
            val l_password1 = g_new_pass?.getText().toString()
            val l_password2 = g_confirm_new_pass?.getText().toString()
            if (l_password1.isEmpty()) //checking wether new password is written in text box or not
            {
                g_new_pass?.setError("Password is required!")
            }
            else if (l_password2.isEmpty())//checking wether confirm new password is written in text box or not
            {
                g_confirm_new_pass?.setError("Password is required!")
            }
            else if (!l_password1.isEmpty() && !l_password2.isEmpty())//if both the text are not empty we willc heck if the password are same or not
            {
                if (l_password1 == l_password2)//if password are same
                {
                    val stringRequest: StringRequest = object : StringRequest(Method.POST, g_SERVER_URL, Response.Listener { response ->
                        try {
                            val jsonObject = JSONObject(response)
                            val Response = jsonObject.getString("response")
                            if (Response == "Password Success")//the php code will return the response that is Password success if user exist and password is changed  in server database
                            {
                                Toast.makeText(this@ResetPasswordActivity2, "Changing Password ", Toast.LENGTH_LONG).show()
                                val i1 = Intent(this@ResetPasswordActivity2, MainActivity::class.java)
                                startActivity(i1)
                            }
                            else
                            {
                                Toast.makeText(this@ResetPasswordActivity2, "Wrong Email id/Password", Toast.LENGTH_LONG).show()
                            }
                        }
                        catch (e: JSONException)
                        {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { Toast.makeText(this@ResetPasswordActivity2, "Connect to Internet & try again", Toast.LENGTH_LONG).show() }) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): Map<String, String> {
                            val params: MutableMap<String, String> = HashMap()
                            //sending the user email and password for changing the password to the php file
                            params["email"] = l_emailuser
                            params["password"] = l_password1
                            return params
                        }
                    }
                    MySingleton.getInstance(this@ResetPasswordActivity2)!!.addToRequestQue(stringRequest)

                }
                else
                {
                    g_confirm_new_pass?.setError("Password doesn't match!")
                }
            }
        }
        else
        {
            Toast.makeText(this@ResetPasswordActivity2, "Connect to Internet & try again", Toast.LENGTH_LONG).show()
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

        const val g_SERVER_URL = "http:/192.168.29.71/poc/resetpassword.php"



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