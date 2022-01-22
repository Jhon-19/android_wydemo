package com.example.wydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始化
        replaceFragment(HomeFragment())
        homeImage.setImageResource(R.drawable.home_clicked)
        homeText.setTextColor(getResources().getColor(R.color.teal_200))
        //绑定点击事件 改变 fragment
        home.setOnClickListener {
            if (homeText.currentTextColor != getResources().getColor(R.color.teal_200)) {
                Log.d("sgsg", "change")
                replaceFragment(HomeFragment())
                homeImage.setImageResource(R.drawable.home_clicked)
                meImage.setImageResource(R.drawable.me)
                homeText.setTextColor(getResources().getColor(R.color.teal_200))
                meText.setTextColor(getResources().getColor(R.color.black))
            }
        }
        me.setOnClickListener {
            if (meText.currentTextColor != getResources().getColor(R.color.teal_200)) {
                Log.d("sgsg", "change")
                replaceFragment(MeFragment())
                homeImage.setImageResource(R.drawable.home)
                meImage.setImageResource(R.drawable.me_clicked)
                homeText.setTextColor(getResources().getColor(R.color.black))
                meText.setTextColor(getResources().getColor(R.color.teal_200))
            }
        }
        addBtn.setOnClickListener {
            // TODO: 2022/1/22 跳到发布页面
            val intent = Intent(this, Publish::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

}