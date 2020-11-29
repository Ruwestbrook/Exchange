package com.russell.exchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.RowId

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val array= arrayOf( "https://youtu.be/uVpe2DV9Moc",
            "https://youtu.be/BkifkPxONLw",
            "https://youtu.be/ljMGpKmRZtA",
            "https://youtu.be/-fh02ToPMPU",
            "https://youtu.be/O2_kwDcykZI",
            "https://youtu.be/zAw5-jlGF54",
            "https://youtu.be/GFbYHxB96fA",
        "https://youtu.be/7Y0rRsvqQc4")


        val recyclerView:RecyclerView=findViewById(R.id.list)
        recyclerView.layoutManager=GridLayoutManager(this,2)
        recyclerView.adapter=object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            )=object : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
            ) {}

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    val intent=Intent(this@VideoActivity,VideoPlayActivity::class.java).apply {
                        putExtra("url",array[position])
                    }
                    startActivity(intent)
                }
            }

            override fun getItemCount()=array.size

        }

    }

    fun back(view: View) {
        finish()
    }
}