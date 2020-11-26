package com.russell.exchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        val list=findViewById<RecyclerView>(R.id.list)
        list.layoutManager= LinearLayoutManager(this)
        val item= DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        item.setDrawable(ContextCompat.getDrawable(this,R.drawable.forum_divider)!!)
        list.addItemDecoration(item)
        list.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view= LayoutInflater.from(parent.context).inflate(
                    R.layout.item_forum,
                    parent,
                    false
                )
                return object : RecyclerView.ViewHolder(view){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    startActivity(Intent(this@ForumActivity,ForumDetailActivity::class.java))
                }

            }

            override fun getItemCount(): Int {
                return 20
            }

        }

        findViewById<TextView>(R.id.new_topic).setOnClickListener {
            startActivity(Intent(this,NewTopicActivity::class.java))
        }

    }
}