package com.shop440.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.shop440.R
import com.shop440.navigation.home.HomeActivityFragment
import com.shop440.navigation.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainNavigation : AppCompatActivity() {
    private val homeActivityFragment = HomeActivityFragment()
    private val profileActivityFragment = ProfileFragment()

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

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
        }.commit()
    }

    object Pager{
        class PagerAdapter(fragment:FragmentManager) : FragmentPagerAdapter(fragment){
            override fun getItem(position: Int): Fragment =
                when(position){
                    0->HomeActivityFragment()
                    2->ProfileFragment()
                    else->HomeActivityFragment()
                }

            override fun getCount() = 3
        }
    }
}
