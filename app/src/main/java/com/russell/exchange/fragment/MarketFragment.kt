package com.russell.exchange.fragment

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russell.exchange.R
import java.io.InputStream

class MarketFragment : Fragment() {

    companion object {
        fun newInstance() = MarketFragment()
    }

    private lateinit var imageList: List<String>
    private lateinit var assetManager: AssetManager

    private var rootView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_top_brokers,container,false)
            assetManager = context!!.assets
            val list = assetManager.list("images")
                imageList = list!!.filter {
                    it.startsWith("u") }
            val recyclerView = rootView?.findViewById<RecyclerView>(R.id.list)
            recyclerView?.layoutManager = LinearLayoutManager(context!!)
            recyclerView?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        return rootView
    }

    private fun getPicture(name: String): Bitmap {
        val inputStream: InputStream = assetManager.open("images/$name")
        return BitmapFactory.decodeStream(inputStream)

    }
}