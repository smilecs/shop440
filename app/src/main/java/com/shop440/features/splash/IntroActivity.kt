package com.shop440.features.splash

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment

import com.github.paolorotolo.appintro.AppIntro
import com.shop440.R

class IntroActivity : AppIntro() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Note here that we DO NOT use setContentView();
        preferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        editor = preferences.edit()
        addSlide(SampleSlide.newInstance(R.layout.splash1))
        addSlide(SampleSlide.newInstance(R.layout.splash2))
        addSlide(SampleSlide.newInstance(R.layout.splash3))

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.GRAY)
        setSeparatorColor(Color.parseColor("#ffffff"))

        // Hide Skip/Done button.
        showSkipButton(true)
        isProgressButtonEnabled = true
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        editor.putBoolean("islogged", true)
        editor.apply()
       // val i = Intent(this, MainActivity::class.java)
     //   startActivity(i)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        editor.putBoolean("islogged", true)
        editor.apply()
      //  val i = Intent(this, MainActivity::class.java)
      //  startActivity(i)
        finish()
    }
}
