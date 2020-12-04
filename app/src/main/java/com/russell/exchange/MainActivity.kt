package com.russell.exchange

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.russell.exchange.fragment.MarketFragment
import com.russell.exchange.fragment.MineFragment
import com.russell.exchange.fragment.ToolFragment
import com.russell.exchange.navigation.BottomNavigationBar
import com.russell.exchange.navigation.NavigationItem
import qiu.niorgai.StatusBarCompat

class MainActivity : AppCompatActivity() {

    private lateinit var bottom_bar:BottomNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_bar=findViewById(R.id.bottom_bar)
        initBar()
        StatusBarCompat.setStatusBarColor(this, Color.WHITE)
    }

    private fun initBar() {

        val home = NavigationItem()
        home.normalDrawable = R.drawable.un_choose_home
        home.chooseDrawable= R.drawable.choose_home
        home.title="Home"
        home.fragment= HomeFragment.newInstance()
        bottom_bar.addItem(home)

        val record = NavigationItem()
        record.normalDrawable = R.drawable.un_choose_record
        record.chooseDrawable= R.drawable.choose_record
        record.title="Market"
        record.fragment= MarketFragment.newInstance()
        bottom_bar.addItem(record)


        val find = NavigationItem()
        find.normalDrawable = R.drawable.un_choose_find
        find.chooseDrawable= R.drawable.choose_find
        find.title="Tools"
        find.fragment= ToolFragment.newInstance()
        bottom_bar.addItem(find)

        val mine = NavigationItem()
        mine.normalDrawable = R.drawable.un_choose_mine
        mine.chooseDrawable= R.drawable.choose_mine
        mine.title="Me"
        mine.fragment= MineFragment.newInstance()


        bottom_bar.addItem(mine)

        bottom_bar.prepare(this,R.id.container)



    }
}