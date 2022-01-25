package com.example.sqllite.StudentPerformance

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.smartscholar.R;

/*
created by Amrit kumar
 */
class MyListAdapter(private val context: Activity, private val title: ArrayList<String>, private val description: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.textView) as TextView

        val subtitleText = rowView.findViewById(R.id.textView2) as TextView

        titleText.text = title[position]

        subtitleText.text = description[position]

        return rowView
    }
}