package com.russell.exchange.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russell.exchange.R

/**
 * @author russell
 * @description:
 * @date : 2020/11/29 12:03
 */
class ArticlesFragment : Fragment() {

    private var recyclerView:RecyclerView?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(recyclerView==null){
            recyclerView= RecyclerView(context!!)
            recyclerView?.layoutManager=LinearLayoutManager(context!!)
            recyclerView?.adapter=object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object :RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
                        R.layout.item_articles_list,parent,false)){}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                }

                override fun getItemCount()= 4

            }
        }

        return recyclerView
    }
}