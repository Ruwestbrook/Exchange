package com.russell.exchange

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ServiceActivity : AppCompatActivity() {
    val list= mutableListOf<Message>()
    private val handler= @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            list.add(Message("Service busy. Please wait a second...", 0))
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        list.add(Message("Hello! I'm your customer service.Is there anything I can help?", 0))
        recyclerView=findViewById(R.id.list)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object :RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        if (viewType == 0) R.layout.item_message_left else R.layout.item_message_right,
                        parent,
                        false
                    )
                ){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView=holder.itemView.findViewById<TextView>(R.id.messgae)
                textView.text=list[position].message
            }

            override fun getItemViewType(position: Int)=list[position].type

            override fun getItemCount()=list.size

        }
    }

    fun back(view: View) {
        finish()
    }
    fun send(view: View) {
        val editText:EditText=findViewById(R.id.send_message)
        val text=editText.text.toString()
        if(text.isEmpty()){
            return
        }
        list.add(Message(text, 1))
        editText.setText("")
        handler.sendEmptyMessageDelayed(1, 1000)
        hideSoftKeyboard(view)
        recyclerView.adapter?.notifyDataSetChanged()

    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)

        }



}



data class Message(val message: String, val type: Int)