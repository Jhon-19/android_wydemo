package com.example.wydemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TestActivity : AppCompatActivity() {
    private var relaAddress = "/whu/lectureList"
    private val args = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        testBtn1.setOnClickListener {
            args.clear()
            val page = 2
            args["page"] = page.toString()
            args["size"] = "40"
            HttpUtil.sendRequestWithOkHttp(relaAddress, args, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    //Log.d("sgsg", "发送请求成功")
                    val responseData: String? = response.body()?.string()
                    if (responseData != null) {
                        Log.d("sgsg", responseData)
                        addData(responseData)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        }

    }

    fun addData(responseData: String) {
        try {
            val jsonObject = JSONObject(responseData)
            val responseData2 = jsonObject.getString("data")
            Log.d("sgsg", responseData2)
            if (responseData2 == "[]") {
                return
            }
            val jsonArray = JSONArray(responseData2)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val time = jsonObject.getString("time")
                val title = jsonObject.getString("title")
                Lecture.data.add(Lecture(time, title))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}