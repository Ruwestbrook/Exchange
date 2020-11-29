package com.russell.exchange

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.russell.exchange.fragment.ArticlesFragment
import com.russell.exchange.fragment.IntroductionFragment


class VActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null

    private var viewpager: ViewPager2? = null
    private val mutableList= mutableListOf<Fragment>()
    private val items= arrayListOf("Introduction","Articles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v)
        tabLayout=findViewById(R.id.tab)
        viewpager=findViewById(R.id.view_pagwer)
        mutableList.add(IntroductionFragment())
        mutableList.add(ArticlesFragment())
        val  fragmentPagerAdapter= object : FragmentStateAdapter(supportFragmentManager,lifecycle){

            override fun getItemCount()= 2

            override fun createFragment(position: Int)= mutableList[position]
        }
        viewpager?.adapter=fragmentPagerAdapter

        TabLayoutMediator(tabLayout!!, viewpager!!) { tab, position ->
            tab.text = items[position]
            viewpager?.currentItem=position
        }.attach()

    }

    fun back(view: View) {
        finish()
    }
}