package com.russell.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
    }

    fun show(view: View) {}
}