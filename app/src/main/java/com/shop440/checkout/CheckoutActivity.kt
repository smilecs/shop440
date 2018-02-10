package com.shop440.checkout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.checkout.kart.BaseKartActivity
import com.shop440.checkout.kart.KartFragment
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseKartActivity(), CheckoutContract.View {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    override lateinit var presenter: CheckoutContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter
        // .
        container.adapter = mSectionsPagerAdapter
        presenter = CheckoutPresenter(this@CheckoutActivity, NetModule.provideRetrofit())
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // checkoutNextButton.text =
            }

            override fun onPageSelected(position: Int) {

            }
        })
        checkoutNextButton.text = getString(R.string.next_checkout_button)
        checkoutNextButton.setOnClickListener {

        }
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_checkout, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCheckOut() {

    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when(position){
                0-> KartFragment()
                else->{
                    OrderSummaryFragment()
                }
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }
}
