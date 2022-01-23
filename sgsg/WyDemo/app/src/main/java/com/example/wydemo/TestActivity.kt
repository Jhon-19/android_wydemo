package com.example.wydemo

import android.content.Context
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
        testBtn1.setOnClickListener {
            save()
        }
        testBtn2.setOnClickListener {
            load()
        }
    }

    private fun save() {
        val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.putString("name", "Tom")
        editor.putInt("age", 28)
        editor.putBoolean("married", false)
        editor.apply()
    }

    private fun load() {
        val data = getSharedPreferences("data", Context.MODE_PRIVATE)
        val name = data.getString("name", "")
        val age = data.getInt("age", 0)
        val married = data.getBoolean("married", false)
        Log.d("sgsg", "name is $name")
        Log.d("sgsg", "age is $age")
        Log.d("sgsg", "married is $married")
    }
}