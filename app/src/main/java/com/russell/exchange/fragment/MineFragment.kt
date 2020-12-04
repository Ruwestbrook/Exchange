package com.russell.exchange.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.russell.exchange.R

/**
@author russell
@description:
@date : 2020/12/5 1:35
 */
class MineFragment :Fragment() {


    private var rootView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.frtagment_mine,container,false)
        }
        return rootView
    }

    companion object {
        fun newInstance() = MineFragment()
    }
}