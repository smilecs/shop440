package com.shop440.features.checkout

import android.app.Activity
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shop440.R
import com.shop440.repository.api.NetModule
import com.shop440.features.auth.AuthActivity
import com.shop440.features.checkout.kart.KartFragment
import com.shop440.features.checkout.models.Order
import com.shop440.repository.dao.models.UserAdress
import com.shop440.utils.PreferenceManager
import com.shop440.utils.ProgressHelper
import com.shop440.viewmodel.KartViewModel
import kotlinx.android.synthetic.main.checkout_container_fragment.*

class CheckoutFragmentContainer : Fragment(), CheckoutContract.View, AddressSheetFragment.OnFragmentAddressListener {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val offScreenLimit = 2
    private val orderSummaryFragment = OrderSummaryFragment()
    private val CHECK_OUT = 8000
    private lateinit var progressDialog: ProgressDialog
    private val userViewModel: KartViewModel by lazy {
        ViewModelProviders.of(this).get(KartViewModel::class.java)
    }


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
        progressDialog = ProgressHelper.progressDialog(context)
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

        val bottomSheetB = BottomSheetBehavior.from(bottomBar)
        val sheetBehaviourCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetB.peekHeight = 0
                }
            }
        }


        bottomSheetB.setBottomSheetCallback(sheetBehaviourCallback)

        checkoutPrevButton.setOnClickListener {
            prev()
        }


        checkoutNextButton.setOnClickListener {
            if (container.currentItem != offScreenLimit - 1) {
                container.run {
                    currentItem += 1
                }
                return@setOnClickListener
            }
            PreferenceManager.PrefData.getPreferenceManager()?.let {
                if (!it.token.isNullOrBlank()) {
                    Log.i("something", "here2222")
                    performCheckout(it)
                    return@setOnClickListener
                }
                startActivityForResult(Intent(context, AuthActivity::class.java), CHECK_OUT)
            }
        }
    }

    private fun performCheckout(prefManager: PreferenceManager?) {
        prefManager?.let {
            if (it.address != null && it.city != null) {
                val order = Order().apply {
                    shopOrders.addAll(orderSummaryFragment.shopOrders)
                    phone = this.phone
                    city = this.city
                    address = this.address
                    total = orderSummaryFragment.amountForTotal
                }
                presenter.checkOut(order, this)
            } else {
                val bottomSheetFrag = AddressSheetFragment.newInstance(this)
                bottomSheetFrag.show(childFragmentManager, "sheet")
            }
        }
    }

    fun prev() {
        container.run {
            if (currentItem != 0) {
                currentItem -= 1
                return
            }
        }
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        if (progressDialog.isShowing) {
            progressDialog.hide()
            return
        }
        Log.i("something", "h")
        progressDialog.show()

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

    override fun onFragmentInteraction(userAdress: UserAdress) {
        PreferenceManager.PrefData.getPreferenceManager()?.run {
            persistAddress(userAdress.address)
            persistCity(userAdress.city)
            performCheckout(this)
        }
    }

    override fun getViewModel() = userViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            performCheckout(PreferenceManager.PrefData.getPreferenceManager())

        }
    }
}
