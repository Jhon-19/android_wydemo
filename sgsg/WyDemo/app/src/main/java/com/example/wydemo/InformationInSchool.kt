package com.example.wydemo

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class InformationInSchool(val time: String, val url: String, val title: String) {

    companion object {
        private var relaAddress = "/whu/notice"
        private val args = HashMap<String, String>()
        private var page = 1
        val data = ArrayList<InformationInSchool>()

        //接收一页数据用于初始化data
        fun init() {
            args.clear()
            args["page"] = "1"
            HttpUtil.sendRequestWithOkHttp(relaAddress, args, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("sgsg", "发送请求成功")
                    val responseData: String? = response.body()?.string()
                    if (responseData != null) {
                        addData(responseData)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        }

        fun append() {

        }

        fun addData(responseData: String) {
//            Log.d("sgsg", responseData)
            try {
                val jsonArray = JSONArray(responseData)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val time = jsonObject.getString("date")
                    val url = jsonObject.getString("href")
                    val title = jsonObject.getString("title")
                    data.add(InformationInSchool(time, url, title))
                    Log.d("sgsg", data.size.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}