package com.example.wydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lecture.*
import kotlinx.android.synthetic.main.activity_lecture_content.*


class LectureContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_content)
        //接收数据
        val time = intent.getStringExtra("time")
        val titleData = intent.getStringExtra("title")
        lectureTitle.setText(titleData)
        //数据初始化
        LectureContent.init(time!!, object : LectureContentDataCallback {
            override fun onFinish(response: ArrayList<LectureContent>) {
                runOnUiThread {
                    val layoutManager = LinearLayoutManager(applicationContext)
                    lectureContentRecyclerView.layoutManager = layoutManager
                    val adapter = LectureContentAdapter(response, object : OnClickCallback {
                        override fun onClick(view: View, position: Int) {
                            Log.d("sgsg", response[position].college)
                        }
                    })
                    lectureContentRecyclerView.adapter = adapter
                }
            }

            override fun onError(e: Exception) {
                e.printStackTrace()
            }

        })
    }
}

