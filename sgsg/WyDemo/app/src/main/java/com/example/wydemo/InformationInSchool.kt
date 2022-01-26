package com.example.wydemo

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.w3c.dom.Text
import java.io.IOException
import androidx.core.content.ContextCompat.startActivity

interface InformationInSchoolDataCallBack {
    fun onFinish(response: ArrayList<InformationInSchool>)
    fun onError(e: Exception)
}


class InformationInSchool(val time: String, val url: String, val title: String) {

    companion object {
        private var relaAddress = "/whu/notice"
        private val args = HashMap<String, String>()
        private var page = 1
        val data = ArrayList<InformationInSchool>()

        //接收一页数据用于初始化data
        fun init(listener: InformationInSchoolDataCallBack) {
            data.clear()
            args.clear()
            args["page"] = "1"
            HttpUtil.sendRequestWithOkHttp(relaAddress, args, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    //Log.d("sgsg", "发送请求成功")
                    val responseData: String? = response.body()?.string()
                    if (responseData != null) {
                        addData(responseData, listener)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    listener.onError(e)
                }
            })
        }

        fun append() {

        }

        fun addData(responseData: String, listener: InformationInSchoolDataCallBack) {
//            Log.d("sgsg", responseData)
            try {
                val jsonArray = JSONArray(responseData)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val time = jsonObject.getString("date")
                    val url = jsonObject.getString("href")
                    val title = jsonObject.getString("title")
                    data.add(InformationInSchool(time, url, title))
                    //Log.d("sgsg", data.size.toString())
                }
                listener.onFinish(data)
            } catch (e: Exception) {
                e.printStackTrace()
                listener.onError(e)
            }
        }
    }
}