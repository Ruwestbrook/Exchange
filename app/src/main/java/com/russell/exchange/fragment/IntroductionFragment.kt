package com.russell.exchange.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.russell.exchange.R

/**
 * @author russell
 * @description:
 * @date : 2020/11/29 12:03
 */
class IntroductionFragment : Fragment(){


    var rootView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_v_details,container,false)
        }
        return rootView
    }
}