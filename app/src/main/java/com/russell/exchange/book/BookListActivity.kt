 package com.russell.exchange.book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russell.exchange.R

 class BookListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val list=findViewById<RecyclerView>(R.id.list)
        list.layoutManager= LinearLayoutManager(this)
        list.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view= LayoutInflater.from(parent.context).inflate(
                    if(viewType==1) R.layout.item_book_left else R.layout.item_book_right,
                    parent,
                    false
                )
                return object : RecyclerView.ViewHolder(view){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    val intent=Intent(this@BookListActivity, BookDetailsActivity::class.java)
                    intent.putExtra("type",position)
                    startActivity(intent)
                }

            }

            override fun getItemCount(): Int {
                return 2
            }

            override fun getItemViewType(position: Int): Int {
                return if(position%2==0) 1 else 0
            }
        }
    }

     fun back(view: View) {
         finish()
     }
 }