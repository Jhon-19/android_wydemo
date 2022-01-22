package com.example.wydemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.me_fragment.*
import kotlinx.android.synthetic.main.me_fragment.view.*

class MeFragment : Fragment() {
    private lateinit var userInfo: TextView
    private lateinit var signUp: Button
    private lateinit var signIn: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.me_fragment, container, false)
        initViews(view)
        signUp.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            startActivity(intent)
        }

        signIn.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun initViews(view: View) {
        userInfo = view.userInfo
        signUp = view.signUp
        signIn = view.signIn
    }

    override fun onResume() {
        super.onResume()
        Log.d("sgsg", "resume")
        if (User.signIn) {
            userInfo.setText("账号: ${User.id}\n" +
                    "信用等级: ${User.creditLevel}\n" +
                    "是否认证: ${if (User.certification) "是" else "否"}")
        }
    }
}
