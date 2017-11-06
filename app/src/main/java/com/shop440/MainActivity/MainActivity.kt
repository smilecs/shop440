package com.shop440.MainActivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.shop440.R
import com.shop440.SearchActivity
import com.shop440.utils.AppEventsLogger
import com.shop440.auth.LoginFragment

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
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
                val i = Intent(this, LoginFragment::class.java)
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
