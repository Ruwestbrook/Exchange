package com.russell.exchange

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream

class TopBrokersActivity : AppCompatActivity() {
    private lateinit var imageList: List<String>
    private lateinit var assetManager: AssetManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_brokers)
        assetManager = assets
        val list = assetManager.list("images")
        if (list?.filter { it.startsWith("u") } == null)
            return
        imageList = list.filter { it.startsWith("u") }
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_broker, parent, false)
                ) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val imageView: ImageView = holder.itemView.findViewById(R.id.image)
                imageView.setImageBitmap(getPicture(imageList[position]))
                val rank: TextView = holder.itemView.findViewById(R.id.rank)
                rank.text = (position + 1).toString()

            }

            override fun getItemCount() = imageList.size

        }


    }

    private fun getPicture(name: String): Bitmap {
        val inputStream: InputStream = assetManager.open("images/$name")
        return BitmapFactory.decodeStream(inputStream)

    }
}