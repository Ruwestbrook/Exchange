package com.russell.exchange

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity



class TradersDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traders_details)
    }

    fun back(view: View) {
        finish()
    }


}