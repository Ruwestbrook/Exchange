package com.russell.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class NewTopicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_topic)
    }

    fun back(view: View) {
        finish()
    }


    fun publish(view: View) {
        Toast.makeText(this,"Successful publish, waiting for background review",Toast.LENGTH_LONG).show()
        finish()
    }
}