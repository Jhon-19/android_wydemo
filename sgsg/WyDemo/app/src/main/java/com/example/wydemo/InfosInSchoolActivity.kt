package com.example.wydemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_infos_in_school.*

class InfosInSchoolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos_in_school)
        InformationInSchool.init(object : InformationInSchoolDataCallBack {
            override fun onFinish(data: ArrayList<InformationInSchool>) {
                runOnUiThread {
                    val layoutManager = LinearLayoutManager(applicationContext)
                    infosRecyclerView.layoutManager = layoutManager
                    val adapter = InfosAdapter(data)
                    infosRecyclerView.adapter = adapter
                }
            }

            override fun onError(e: Exception) {
                e.printStackTrace()
            }

        })
    }
}