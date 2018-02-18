package com.shop440.checkout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.checkout.kart.KartFragment
import kotlinx.android.synthetic.main.checkout_container_fragment.*

class CheckoutFragmentContainer : Fragment(), CheckoutContract.View {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val offScreenLimit = 2
    private val orderSummaryFragment = OrderSummaryFragment()


    override lateinit var presenter: CheckoutContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.checkout_container_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        // Set up the ViewPager with the sections adapter
        // .
        container.adapter = mSectionsPagerAdapter
        viewPagerIndicator.setupWithViewPager(container)
        presenter = CheckoutPresenter(this@CheckoutFragmentContainer, NetModule.provideRetrofit())
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                checkoutNextButton.text = when (position) {
                    1 -> getString(R.string.order_finish_button)
                    else -> {
                        getString(R.string.next_checkout_button)
                    }
                }
            }
        })

        checkoutPrevButton.setOnClickListener {
            prev()
        }


        checkoutNextButton.setOnClickListener {
            if (container.currentItem != offScreenLimit) {
                container.run {
                    currentItem += 1
                }
                return@setOnClickListener
            }
          //  presenter.checkOut()
        }

    }

    fun prev(){
        container.run {
            if (currentItem != 0) {
                currentItem -= 1
                return
            }
        }
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun onCheckOut() {
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> KartFragment()
                else -> {
                    orderSummaryFragment
                }
            }
        }

        override fun getCount(): Int {
            return offScreenLimit
        }
    }
}
