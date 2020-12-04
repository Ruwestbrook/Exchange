package com.russell.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private var rootView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_transaction,container,false)
        }
        return rootView
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}