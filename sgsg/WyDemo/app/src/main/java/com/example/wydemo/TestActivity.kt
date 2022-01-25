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
import java.io.IOException

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val banner: Banner<DataBean, BannerImageAdapter<DataBean>> = findViewById(R.id.banner)

        banner
            .setAdapter(object : BannerImageAdapter<DataBean>(DataBean.testData3) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: DataBean,
                    position: Int,
                    size: Int,
                ) {
                    //图片加载自己实现
                    Glide.with(holder.itemView)
                        .load(data.imageUrl)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .into(holder.imageView)
                }
            })
            .addBannerLifecycleObserver(this).setIndicator(CircleIndicator(this))

    }
}