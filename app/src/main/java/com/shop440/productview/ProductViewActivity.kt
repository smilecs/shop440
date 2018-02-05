package com.shop440.productview

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.checkout.CheckoutActivity
import com.shop440.kart.Item
import com.shop440.kart.KartViewModel
import com.shop440.kart.ShopOrders
import com.shop440.dao.models.Image
import com.shop440.dao.models.ProductFeed
import com.shop440.utils.Metrics
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_product_view.*
import kotlinx.android.synthetic.main.activity_product_view_sub_container.*
import kotlinx.android.synthetic.main.activity_product_view_sub_description.*
import kotlinx.android.synthetic.main.bottom_product_view.*
import java.io.File


class ProductViewActivity : AppCompatActivity(), OnMapReadyCallback, ProductViewContract.View {
    override lateinit var presenter: ProductViewContract.Presenter
    var TAG = "ProductViewActivity"

    lateinit var productModel: ProductFeed
    private val kartViewModel: KartViewModel by lazy {
        ViewModelProviders.of(this).get(KartViewModel::class.java)
    }

    private lateinit var coord: LatLng
    private var bundle: Bundle? = null
    private var data: String? = null
    private lateinit var progressDialog: ProgressDialog
    private var totalItems: Int = 0
    private var totalPrice: Double = 0.0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundle = outState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = savedInstanceState
        setContentView(R.layout.activity_product_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        if (intent != null && !intent.dataString.isNullOrBlank()) {
            data = intent.dataString
        }
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage(getString(R.string.loading))
        ProductViewPresenter(this, NetModule.provideRetrofit())

        map.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + productModel.location.lat + "," + productModel.location.lon))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        loadData()
    }



    override fun onMapReady(googleMap: GoogleMap) {
        val builder = LatLngBounds.builder()
        builder.include(coord)
        googleMap.addMarker(MarkerOptions().position(coord).title(productModel.shop.title))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15f))
        map.onResume()
        progressDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.productview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.download) {
            try {
                //presenter.downloadImage(productModel?.image.path, productModel.name, fileCache)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        if (data != null) {
            val resolvedUrl: String = data!!.substring(data!!.lastIndexOf("/") + 1)
            presenter.loadData(resolvedUrl)
        } else {
            productModel = intent.getSerializableExtra("data") as ProductFeed
            initUi()
        }
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        if (progressDialog.isShowing) {
            progressDialog.hide()
            return
        }
        progressDialog.show()

    }

    override fun showProduct(product: ProductFeed) {
        this@ProductViewActivity.productModel = product
        initUi()
    }

    override fun imageDownloaded(filePath: File) {
        runOnUiThread { Toast.makeText(this@ProductViewActivity, "Product Image saved to shop440" + " " + filePath.absolutePath, Toast.LENGTH_LONG).show() }
    }

    internal class ViewAdapter(fm: FragmentManager, val image: List<Image>) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int = image.size

        override fun getItem(position: Int): Fragment = PagerGalleryFragment.newInstance(image[position].url, image[position].placeholder)
    }

    public override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun getViewModel(): KartViewModel = kartViewModel

    override fun cartLoaded(realmResults: RealmResults<Item>?) {
        totalPrice = 0.0
        realmResults?.let {
            for (item: Item in it) {
                totalPrice += item.totalPrice
            }
            totalItems = realmResults.size
        }
        cartItems.text = getString(R.string.cart_hint, totalItems.toString(), Metrics.getDisplayPriceWithCurrency(this, totalPrice))
    }

    override fun categoryNameResolved(category: String) {
        productViewCategory.text = category
    }

    override fun shopOrder(shopOrders: ShopOrders) {

    }

    private fun initUi() {
        presenter.resolveCategory(productModel.category)

        if (!productModel.location.lat.isEmpty() && !productModel.location.lon.isEmpty()) {
            coord = LatLng(productModel.location.lat.toDouble(), productModel.location.lon.toDouble())
            map.visibility = View.VISIBLE
            //progressDialog.show()
            val handler = Handler()
            handler.postDelayed({
                map.onCreate(bundle)
                map.getMapAsync(this@ProductViewActivity)
            }, 1800)
        }

        productModel.images?.let {
            imagePager.clipToPadding = false
            imagePager.setPadding(12, 0, 12, 0)
            imagePager.adapter = ViewAdapter(supportFragmentManager, it)
            viewPagerIndicator.setupWithViewPager(imagePager)
        }

        //subContainer views
        productViewTitle.text = productModel.productName
        productViewCity.text = productModel.city
        //productViewCategory.text = productModel.category
        productViewShopName.text = productModel.shop.title
        descriptionProductText.text = productModel.productDesc


        productModel.shop.apply {
            shopNameProductView.text = title
            shopAddressProductView.text = address
            phoneTextProductView.text = phone
        }
        bottomSheetControls()
        presenter.loadCart(this)
    }

    private fun bottomSheetControls() {
        productViewPrice.text = Metrics.getDisplayPriceWithCurrency(this, productModel.productPrice)
        //sheetContainer.visibility = View.GONE
        val behaviour = BottomSheetBehavior.from(bottomBar)
        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                //BottomSheetBehavior.STATE_EXPANDED->imageToggle.setImageDrawable(getDrawable(R.drawable.ic_arrow_downward_black))
                //BottomSheetBehavior.STATE_COLLAPSED->imageToggle.setImageDrawable(getDrawable(R.drawable.ic_arrow_upward_black))
                }
            }
        })
        bottomBar.setOnClickListener {
            if (behaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
                behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                return@setOnClickListener
            }
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        scrollContainer.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                v?.apply {
                    val lastChild = getChildAt(childCount - 1) as View
                    val diff = lastChild.bottom - (height + scrollY)
                    if (diff != 0) {
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        })
        bottomSheetAddCartButton.setOnClickListener {
            presenter.addToCart(productModel)
        }

        bottomSheetCheckout.setOnClickListener {
            startActivity(Intent(this@ProductViewActivity, CheckoutActivity::class.java))
        }

    }

}
