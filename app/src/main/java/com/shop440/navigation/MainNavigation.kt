package com.shop440.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.shop440.R
import com.shop440.navigation.home.HomeActivityFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainNavigation : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> return@OnNavigationItemSelectedListener true
            R.id.navigation_dashboard -> return@OnNavigationItemSelectedListener true
            R.id.navigation_notifications -> return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, HomeActivityFragment())
        }.commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
      //  val editText = searchViewQuery.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
       // editText.setHintTextColor(resources.getColor(R.color.colorAccent))
        //searchViewQuery.clearFocus()
        //appbar.requestFocus()
    }
}
