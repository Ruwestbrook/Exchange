package com.russell.exchange.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russell.exchange.R

/**
@author russell
@description:
@date : 2020/11/29 18:59
 */
class TopPlayersFragment :Fragment() {

    private var rootView: View?=null
    private val rankMoney=arrayOf(
        52802.11,
        34108.09,
        10248.33,
        8528.68,
        7018.05,
        4418.11,
        3408.63,
        2811.50,
        2042.62,
        1438.33
    )

    private val userName= mutableListOf("Namtruc24","Budidhar9","vothantung",
        "frankkn","CindyXXXX","HiddenGap","alexm","teapeak","vvFish","Lizardman")
    private var recyclerView: RecyclerView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            userName.shuffle()
            rootView=layoutInflater.inflate(R.layout.fragment_top_rank, container, false)
            val firstName=rootView?.findViewById<TextView>(R.id.one)
            val firstMoney=rootView?.findViewById<TextView>(R.id.one_money)
            firstName?.text=userName[0]
            firstMoney?.text=rankMoney[0].toString()



            val secondName=rootView?.findViewById<TextView>(R.id.two)
            val secondMoney=rootView?.findViewById<TextView>(R.id.two_money)
            secondName?.text=userName[1]
            secondMoney?.text=rankMoney[1].toString()


            val thirdName=rootView?.findViewById<TextView>(R.id.three)
            val thirdMoney=rootView?.findViewById<TextView>(R.id.three_money)
            thirdName?.text=userName[2]
            thirdMoney?.text=rankMoney[2].toString()


            recyclerView=rootView?.findViewById(R.id.list)
            recyclerView = rootView?.findViewById(R.id.list)
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object : RecyclerView.ViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.item_top,
                            parent,
                            false
                        )
                    ) {}
                }

                @SuppressLint("SetTextI18n")
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val name=holder.itemView.findViewById<TextView>(R.id.name)
                    val money=holder.itemView.findViewById<TextView>(R.id.money)
                    val rank=holder.itemView.findViewById<TextView>(R.id.rank)
                    name.text=userName[position+3]
                    money.text=rankMoney[position+3].toString()+"USD"
                    rank.text="0${position+3}"

                }

                override fun getItemCount() = 7

            }
        }
        return rootView
    }

}