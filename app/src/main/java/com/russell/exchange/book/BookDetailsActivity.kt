package com.russell.exchange.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.russell.exchange.R

class BookDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)


    }

    fun back(view: View) {
        finish()
    }
}