package com.russell.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForexCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forex_calendar)
        val recyclerView=findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_calendar,
                        parent,
                        false
                    )
                ){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            }

            override fun getItemCount()=2

        }

    }

    fun back(view: View) {
        finish()
    }
}