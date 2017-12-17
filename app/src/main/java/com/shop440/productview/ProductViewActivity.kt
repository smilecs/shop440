package com.shop440.productview

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import com.facebook.ads.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.shop440.R
import com.shop440.adapters.GalleryAdapter
import com.shop440.api.NetModule
import com.shop440.models.Image
import com.shop440.models.ProductFeed
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.bottom_product_view.*
import kotlinx.android.synthetic.main.activity_product_view.*
import kotlinx.android.synthetic.main.activity_product_view_sub_container.*
import kotlinx.android.synthetic.main.activity_product_view_sub_description.*
import java.io.File
import java.util.*


class ProductViewActivity : AppCompatActivity(), OnMapReadyCallback, ProductViewContract.View {
    override lateinit var presenter: ProductViewContract.Presenter
    var TAG = "ProductViewActivity"

    lateinit var productModel: ProductFeed

    private lateinit var coord: LatLng
    private var bundle: Bundle? = null
    private lateinit var productView: ProductViewActivity
    private var data: String? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var nativeAd: NativeAd

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundle = outState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = savedInstanceState
        productView = this
        setContentView(R.layout.activity_product_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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


    private fun initUi() {

        if (!productModel.location.lat.isEmpty() && !productModel.location.lon.isEmpty()) {
            coord = LatLng(productModel.location.lat.toDouble(), productModel.location.lon.toDouble())
            map.visibility = View.VISIBLE
            progressDialog.show()
            val handler = Handler()
            handler.postDelayed({
                map.onCreate(bundle)
                map.getMapAsync(productView)
            }, 1800)
        }

        productModel.images?.let {
            imagePager.clipToPadding = false
            imagePager.setPadding(12, 0, 12, 0)
            imagePager.adapter = ViewAdapter(supportFragmentManager, it)
        }
        //subContainer views
        productViewTitle.text = productModel.productName
        productViewCity.text = productModel.city
        productViewCategory.text = productModel.category
        productViewShopName.text = productModel.shop.title
        descriptionProductText.text = productModel.productDesc

        productModel.shop.apply {
            shopNameProductView.text = title
            shopAddressProductView.text = address
            phoneTextProductView.text = phone
        }

        productViewPrice.text = Metrics.getDisplayPriceWithCurrency(this, productModel.productPrice)


        //  showNativeAd()
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

    /*  private fun showNativeAd() {
          nativeAd = NativeAd(this, "909211035848244_1018000754969271")
          nativeAd.setAdListener(object : AdListener {

              override fun onError(ad: Ad, error: AdError) {
                  // Ad error callback
              }

              override fun onAdLoaded(ad: Ad) {
                  nativeAd.unregisterView()
                  // Add the Ad view into the ad container.
                  val nativeAdContainer = findViewById<LinearLayout>(R.id.native_ad_container) as LinearLayout
                  val inflater = LayoutInflater.from(this@ProductViewActivity)
                  // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                  val adView = inflater.inflate(R.layout.ad_layout, nativeAdContainer, false)
                  nativeAdContainer.addView(adView)

                  // Create native UI using the ad metadata.
                  val nativeAdIcon = adView.findViewById<ImageView>(R.id.native_ad_icon) as ImageView
                  val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title) as TextView
                  val nativeAdMedia = adView.findViewById<MediaView>(R.id.native_ad_media) as MediaView
                  val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context) as TextView
                  val nativeAdBody = adView.findViewById<TextView>(R.id.native_ad_body) as TextView
                  val nativeAdCallToAction = adView.findViewById<Button>(R.id.native_ad_call_to_action) as Button

                  // Set the Text.
                  nativeAdTitle.text = nativeAd.adTitle
                  nativeAdSocialContext.text = nativeAd.adSocialContext
                  nativeAdBody.text = nativeAd.adBody
                  nativeAdCallToAction.text = nativeAd.adCallToAction

                  // Download and display the ad icon.
                  val adIcon = nativeAd.adIcon
                  NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon)

                  // Download and display the cover image.
                  nativeAdMedia.setNativeAd(nativeAd)

                  // Add the AdChoices icon
                  val adChoicesContainer = findViewById<LinearLayout>(R.id.ad_choices_container) as LinearLayout
                  val adChoicesView = AdChoicesView(this@ProductViewActivity, nativeAd, true)
                  adChoicesContainer.addView(adChoicesView)

                  // Register the Title and CTA button to listen for clicks.
                  val clickableViews = ArrayList<View>()
                  clickableViews.add(nativeAdTitle)
                  clickableViews.add(nativeAdCallToAction)
                  nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews)
              }

              override fun onAdClicked(ad: Ad) {
                  // Ad clicked callback
              }

              override fun onLoggingImpression(ad: Ad) {

              }
          })

          // Request an ad
          nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL)
      }
      */

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

        override fun getItem(position: Int): Fragment = PagerGalleryFragment.newInstance(image[position].url)
    }

    public override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }
}
