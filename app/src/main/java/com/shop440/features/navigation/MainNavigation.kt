package com.shop440.features.navigation

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import com.google.firebase.iid.FirebaseInstanceId
import com.shop440.Application
import com.shop440.R
import com.shop440.features.checkout.CheckoutFragmentContainer
import com.shop440.features.navigation.home.HomeActivityFragment
import com.shop440.features.navigation.profile.ProfileContainerFragment
import com.shop440.notification.ApiRequest
import com.shop440.notification.DeviceModel
import com.shop440.repository.api.NetModule
import com.shop440.repository.api.response.GenericResponse
import com.shop440.utils.PreferenceManager
import com.shop440.utils.ProgressHelper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainNavigation : AppCompatActivity(), AppContract.AppView {
    lateinit override var presenter: AppContract.Presenter
    private lateinit var progressDialog: ProgressDialog

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> return@OnNavigationItemSelectedListener navItemSelector(0)
            R.id.navigation_dashboard -> return@OnNavigationItemSelectedListener navItemSelector(2)
            R.id.cart -> return@OnNavigationItemSelectedListener navItemSelector(1)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            // set an exit transition
            exitTransition = android.transition.Explode()
        }

        setContentView(R.layout.activity_main)
        navigationViewPager.adapter = Pager.PagerAdapter(supportFragmentManager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationViewPager.currentItem = 0
        navigationViewPager.offscreenPageLimit = 3
        progressDialog = ProgressHelper.progressDialog(this)
        AppPresenter(NetModule.provideRetrofit(), this)
        presenter.start()
    }

    private fun navItemSelector(pos: Int): Boolean {
        navigationViewPager.currentItem = pos
        return true
    }

    object Pager {
        class PagerAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {
            override fun getItem(position: Int): Fragment =
                    when (position) {
                        0 -> HomeActivityFragment()
                        1 -> CheckoutFragmentContainer()
                        else -> ProfileContainerFragment()
                    }

            override fun getCount() = 3


        }
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {
        if (progressDialog.isShowing) {
            progressDialog.hide()
            return
        }
        progressDialog.show()

    }
}
