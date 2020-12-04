package com.russell.exchange

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForexTradersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forex_traders)
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {

                val imageView=ImageView(parent.context)
                imageView.layoutParams=RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
                val padding=dp2Px(15)
                imageView.setPadding(padding,0,padding,0)

                return object : RecyclerView.ViewHolder(
                    imageView
                ) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val imageView: ImageView = holder.itemView as ImageView
                imageView.setImageResource(if(position%2==0) R.drawable.u293 else R.drawable.u294)

            }

            override fun getItemCount() = 10

        }



    }

    fun dp2Px( dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5).toInt()
    }
}