package com.russell.exchange

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForumDetailActivity : AppCompatActivity() {
    private var like=false
    private val mutableList=mutableListOf <String>()
    private lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_detail)
        recyclerView=findViewById(R.id.list)
        mutableList.add("Totally agree.")
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
               return object :RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_forum_comment,parent,false)){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView=holder.itemView.findViewById<TextView>(R.id.details)
                textView.text=mutableList[position]
            }

            override fun getItemCount()=mutableList.size

        }
    }

    fun back(view: View) {
        finish()
    }
    fun report(view: View) {
        Toast.makeText(this,"Report successful, waiting for background review",Toast.LENGTH_SHORT).show()
    }
    fun like(view: View) {
        val textView=view as TextView
        val drawable:Drawable
        drawable= if(like){
            textView.text=(textView.text.toString().toInt()-1).toString()
            ContextCompat.getDrawable(this,R.drawable.un_like)!!;
        }else{
            textView.text=(textView.text.toString().toInt()+1).toString()
            ContextCompat.getDrawable(this,R.drawable.like)!!;
        }
        textView.setCompoundDrawables(drawable,null,null,null)
        like=!like
    }
    fun send(view: View) {
        val textView=findViewById<TextView>(R.id.comment_text)
        val editText=findViewById<EditText>(R.id.send_message)
        if(editText.text.isNotEmpty()){
            mutableList.add(editText.text.toString())
            recyclerView.adapter?.notifyDataSetChanged()
            textView.text=(textView.text.toString().toInt()+1).toString()
        }

    }






}