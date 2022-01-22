package com.example.wydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val address = "http://192.168.0.107/get_data.json"
        val args = HashMap<String, String>()
        testBtn.setOnClickListener {
            HttpUtil.sendRequestWithOkHttp(address, args, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("sgsg", "test")
                    val responseData = response.body()?.string()
                    // TODO: 2022/1/15
                    if (responseData != null) Log.d("sgsg", responseData)
                }

                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}