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
import com.shop440.*
import com.shop440.Login.LoginActivity
import com.shop440.Utils.AppEventsLogger
import com.shop440.Utils.VolleySingleton

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var volleySingleton: VolleySingleton
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
            val i = Intent(this@MainActivity, NewStoreActivity::class.java)
            startActivity(i)
        }
        if (findViewById(R.id.container) != null) {
            val mn = MainActivityFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, mn).commit()
        }

        volleySingleton = VolleySingleton.getsInstance()

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
                val i = Intent(this, ProfileActivity::class.java)
                startActivity(i)
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
