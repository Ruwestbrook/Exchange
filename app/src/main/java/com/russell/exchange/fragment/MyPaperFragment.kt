package com.russell.exchange.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.russell.exchange.R

/**
@author russell
@description:TopPlayersFragment
@date : 2020/11/29 17:50
 */
class MyPaperFragment :Fragment() {


    private var tabLayout: TabLayout? = null

    private var viewpager: ViewPager2? = null
    private var rootView: View? = null
    private val mutableList= mutableListOf<Fragment>()
    private val items= arrayListOf("Position","Ordering","History")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(rootView==null){
            rootView=layoutInflater.inflate(R.layout.fragment_my_paper,container,false)
            tabLayout=rootView?.findViewById(R.id.tab)
            viewpager=rootView?.findViewById(R.id.view_pagwer)
            mutableList.add(OrderingFragment())
            mutableList.add(OrderingFragment())
            mutableList.add(OrderingFragment())
            val  fragmentPagerAdapter= object : FragmentStateAdapter(childFragmentManager,lifecycle){

                override fun getItemCount()= 3

                override fun createFragment(position: Int)= mutableList[position]
            }
            viewpager?.adapter=fragmentPagerAdapter

            TabLayoutMediator(tabLayout!!, viewpager!!) { tab, position ->
                tab.text = items[position]
                viewpager?.currentItem=position
            }.attach()
        }

        return rootView
    }


}