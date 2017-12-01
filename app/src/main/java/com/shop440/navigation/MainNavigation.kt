package com.shop440.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.shop440.R
import com.shop440.navigation.home.HomeActivityFragment
import com.shop440.navigation.profile.BlankSessionFragment
import com.shop440.navigation.profile.ProfileSessionFragment
import com.shop440.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainNavigation : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> navigationViewPager.currentItem = 0
            R.id.navigation_dashboard -> navigationViewPager.currentItem = 2
            R.id.navigation_notifications -> return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationViewPager.adapter = Pager.PagerAdapter(supportFragmentManager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    public fun switch() {
        navigationViewPager.currentItem = 0
    }

    object Pager {
        class PagerAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {
            private val userToken = PreferenceManager.PrefData.getPreferenceManager()?.getSavedToken()
            override fun getItem(position: Int): Fragment =
                    when (position) {
                        0 -> HomeActivityFragment()
                        2 -> profileFragmentChoice()
                        else -> HomeActivityFragment()
                    }

            override fun getCount() = 3

            private fun profileFragmentChoice(): Fragment =
                    if (userToken.isNullOrEmpty()) {
                        BlankSessionFragment()
                    } else {
                        ProfileSessionFragment()
                    }
        }
    }
}
