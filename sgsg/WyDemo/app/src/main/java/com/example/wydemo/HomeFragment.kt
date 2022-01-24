package com.example.wydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        initView(view)
//        activity?.setActionBar(view.toolbar)
        return view
    }

    private fun initView(view: View) {
        toolbar = view.toolbar
    }

}
