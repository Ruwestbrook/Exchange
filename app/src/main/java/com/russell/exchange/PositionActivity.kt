package com.russell.exchange

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.russell.exchange.fragment.MyPaperFragment
import com.russell.exchange.fragment.TopPlayersFragment

class PositionActivity : AppCompatActivity() {

    private val myPaperFragment=MyPaperFragment()
    private val topPlayersFragment= TopPlayersFragment()

    private var pager:TextView?=null
    private var player:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_position)
        player=findViewById(R.id.players)
        player?.setOnClickListener {
            player(it)
        }
        pager=findViewById(R.id.pager)
        pager?.setOnClickListener {
            pager(it)
        }
        supportFragmentManager.beginTransaction().add(R.id.container,myPaperFragment).add(R.id.container,topPlayersFragment)
            .show(myPaperFragment).hide(topPlayersFragment).commit()

    }

    fun player(view: View) {
        player?.setTextColor(Color.WHITE)
        player?.background=ContextCompat.getDrawable(this,R.drawable.bg_player)
        pager?.setTextColor(Color.parseColor("#333333"))
        pager?.background=ContextCompat.getDrawable(this,R.drawable.bg_pager_un)
        supportFragmentManager.beginTransaction()
            .show(topPlayersFragment).hide(myPaperFragment).commit()
    }
    fun pager(view: View) {
        player?.setTextColor(Color.parseColor("#333333"))
        player?.background=ContextCompat.getDrawable(this,R.drawable.bg_player_un)
        pager?.setTextColor(Color.WHITE)
        pager?.background=ContextCompat.getDrawable(this,R.drawable.bg_pager)
        supportFragmentManager.beginTransaction()
            .show(myPaperFragment).hide(topPlayersFragment).commit()
    }
}