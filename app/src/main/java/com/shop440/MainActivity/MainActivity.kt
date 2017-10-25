package com.shop440.MainActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.shop440.IntroActivity
import com.shop440.Login.LoginActivity
import com.shop440.R
import com.shop440.SearchActivity
import com.shop440.Utils.AppEventsLogger

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        sharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("islogged", false)) {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {

        }
        if (findViewById(R.id.container) != null) {
            val mn = MainActivityFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, mn).commit()
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        AppEventsLogger.logMainActivityOpenedEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId


        if (id == R.id.profile) {
            if (sharedPreferences.getString("tokens", "Null") != "Null") {

            } else {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }

            return true
        }

        if (id == R.id.search) {
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }
}
