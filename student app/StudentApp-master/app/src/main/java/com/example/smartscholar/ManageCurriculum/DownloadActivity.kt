package com.example.sqllite


import com.example.smartscholar.R;
import com.example.smartscholar.sqlite;
import android.Manifest
import android.app.ActivityManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.smartscholar.MainActivity
import java.io.File
import java.nio.file.Path

/*
Created by Divyanshu Gupta
In this video we are downloading the selected file and updating the downloaded file link in content table
 */
class DownloadActivity : AppCompatActivity() {


    private var downloadManager: DownloadManager? = null
    private var g_refid: Long = 0
    private var g_Download_Uri: Uri? = null
    var l_content_link: String? = null
    var file:File?=null
    var l_content_id: String? = null
    var db: sqlite? = null
    var g_keyword:String?="/exoplayer-test-media-0/"
    var g_list: ArrayList<Long> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        l_content_link = intent.getStringExtra("CT_LINK")
        l_content_id = intent.getStringExtra("CT_ID")
        db = sqlite(this)



        registerReceiver(onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


    }


    fun download(view: View?) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                var filename = l_content_link!!.substring(l_content_link!!.indexOf(g_keyword!!) + g_keyword!!.length)
                downloadit(filename)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            var filename =l_content_link!!.substring(l_content_link!!.indexOf(g_keyword!!)+g_keyword!!.length)
            downloadit(filename)
        }

    }
    fun downloadit(filename: String) {
        g_Download_Uri = Uri.parse(l_content_link)
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try{
            if(manager!=null){
                val request = DownloadManager.Request(g_Download_Uri)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

                request.setAllowedOverRoaming(false)
                request.setTitle(filename)
                request.setDescription("CONTENT"+filename)
                request.setAllowedOverMetered(true)
                request.setAllowedOverRoaming(true)
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
                request.setMimeType(getMimeType(g_Download_Uri))
                g_refid = manager.enqueue(request)
                g_list.add(g_refid);
                Toast.makeText(this@DownloadActivity,"Download Started",Toast.LENGTH_SHORT).show()
               file = File(Environment.getExternalStorageDirectory().absolutePath+"/Download/",filename)


                db?.updateContentTable(file.toString(),l_content_id.toString())
            }
            else{
                var intent= Intent(Intent.ACTION_VIEW,g_Download_Uri)
                startActivity(intent)
            }
        }catch (e:Exception){
            Toast.makeText(this@DownloadActivity,"Something went wrong!",Toast.LENGTH_SHORT).show()
            Log.e("ERRO:MAIN","E: "+e.message)

        }



    }


    var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.e("IN", "" + referenceId)
            g_list.remove(referenceId)
            if (g_list.isEmpty()) {
                Log.e("INSIDE", "" + referenceId)
                val mBuilder = NotificationCompat.Builder(this@DownloadActivity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Download")
                        .setContentText("All Download completed")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(455, mBuilder.build())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            var filename = l_content_link!!.substring(l_content_link!!.indexOf(g_keyword!!) + g_keyword!!.length)
            downloadit(filename)

            // permission granted
        }
        else{
            Toast.makeText(this@DownloadActivity,"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }

fun getMimeType(uri: Uri?): String? {
        var resolver= contentResolver
   var mimeTypeMap=MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri!!))
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




