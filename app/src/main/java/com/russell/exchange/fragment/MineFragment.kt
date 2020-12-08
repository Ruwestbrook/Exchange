package com.russell.exchange.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import com.russell.exchange.*
import com.russell.exchange.book.BookListActivity

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
            rootView?.findViewById<ConstraintLayout>(R.id.mine_mira)?.apply{
              setOnClickListener {
                  startActivity(Intent(context,BookListActivity::class.java))
              }
          }
            rootView?.findViewById<ConstraintLayout>(R.id.mine_qa)?.apply{
                setOnClickListener {
                    startActivity(Intent(context,ForexTradersActivity::class.java))
                }
            }
            rootView?.findViewById<ConstraintLayout>(R.id.mine_service)?.apply{
                setOnClickListener {
                    startActivity(Intent(context,VideoActivity::class.java))
                }
            }
              rootView?.findViewById<ConstraintLayout>(R.id.mine_about)?.apply{
                setOnClickListener {
                    startActivity(Intent(context,VActivity::class.java))
                }
            }


            rootView?.findViewById<ConstraintLayout>(R.id.mine_setting)?.apply{
                setOnClickListener {
                    startActivity(Intent(context, ForexCalendarActivity::class.java))
                }
            }
            rootView?.findViewById<ConstraintLayout>(R.id.mine_services)?.apply{
                setOnClickListener {
                    startActivity(Intent(context, ServiceActivity::class.java))
                }
            }
            rootView?.findViewById<ConstraintLayout>(R.id.forum)?.apply{
                setOnClickListener {
                    startActivity(Intent(context, ForumActivity::class.java))
                }
            }

            rootView?.findViewById<ConstraintLayout>(R.id.margin)?.apply{
                setOnClickListener {
                    startActivity(Intent(context, MarginCalculatorActivity::class.java))
                }
            }
        }
        return rootView
    }

    companion object {
        fun newInstance() = MineFragment()
    }
}