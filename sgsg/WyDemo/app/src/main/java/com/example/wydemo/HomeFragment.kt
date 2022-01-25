package com.example.wydemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.home_fragment.view.*

import kotlinx.android.synthetic.main.activity_test.view.*


class HomeFragment : Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var schools: Spinner
    private lateinit var search: RelativeLayout
    private lateinit var msg: ImageButton
    private lateinit var rotationMap: Banner<DataBean, BannerImageAdapter<DataBean>>
    private val schoolList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)
        initView(view)
        initSchools()

        //轮播图
        rotationMap
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
                        .into(holder.imageView)
                }
            })
            .addBannerLifecycleObserver(this).setIndicator(CircleIndicator(activity))

        //spinner下拉选项
        val adapter: SpinnerAdapter =
            ArrayAdapter<String>(requireActivity(),
                R.layout.my_spinner_layout,
                schoolList)

        schools.adapter = adapter
        schools.setSelection(0)  //设置默认选中项
        schools.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                Toast.makeText(activity, "选中了${schoolList[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        //跳转搜索页面
        search.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        //跳转消息页面
        msg.setOnClickListener {
            val intent = Intent(activity, MessageActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun initView(view: View) {
        toolbar = view.toolbar
        schools = view.schools
        search = view.search
        msg = view.msg

        rotationMap = view.rotationMap as Banner<DataBean, BannerImageAdapter<DataBean>>
    }

    private fun initSchools() {
        schoolList.add("武大")
        schoolList.add("华科")
        schoolList.add("武理")
    }

}
