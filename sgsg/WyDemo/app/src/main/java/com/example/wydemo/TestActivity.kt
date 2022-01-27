package com.example.wydemo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test.*
import android.app.Dialog
import android.widget.ImageView


class TestActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var image: ImageView

    private var relaAddress = "/whu/lectureList"
    private val args = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        init()
        testBtn1.setOnClickListener {}
        imageView.setOnClickListener {
            dialog?.show()
        }

    }

    private fun init() {

        //展示在dialog上面的大图
        dialog = Dialog(this, R.style.FullActivity)
        image = ImageView(this)
        image.setImageResource(R.drawable.lecture_image)
        dialog.setContentView(image)

        image.setOnClickListener { dialog.dismiss() }
    }

}