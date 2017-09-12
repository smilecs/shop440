package com.shop440.ProductView

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import butterknife.ButterKnife
import butterknife.OnClick
import com.android.volley.toolbox.ImageLoader
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.ads.*
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.ShareMediaContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.shop440.Api.NetModule
import com.shop440.Models.Datum
import com.shop440.Models.StoreModel
import com.shop440.R
import com.shop440.StoreActivity
import com.shop440.Utils.AppEventsLogger
import com.shop440.Utils.FileCache
import com.shop440.Utils.VolleySingleton
import kotlinx.android.synthetic.main.activity_product_view.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*


class ProductViewActivity : AppCompatActivity(), OnMapReadyCallback, ProductViewContract.View {
    override lateinit var presenter: ProductViewContract.Presenter
    var TAG = "ProductViewActivity"
    lateinit var fileCache: FileCache
    lateinit var productModel: Datum
    lateinit var content: ShareLinkContent
    lateinit var callbackManager: CallbackManager
    lateinit var shareDialog: ShareDialog
    lateinit var sharePhoto: SharePhoto.Builder
    lateinit var photo: SharePhoto
    var next = true
    lateinit var map: MapView
    private lateinit var coord: LatLng
    private lateinit var imageLoader: ImageLoader
    private var bundle: Bundle? = null
    private lateinit var productView: ProductViewActivity
    private lateinit var data: String
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
        map = findViewById(R.id.map) as MapView
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        fileCache = FileCache(this)
        val intent = intent
        if (intent.dataString != null) {
            data = intent.dataString
        }
        imageLoader = VolleySingleton.getsInstance().imageLoader
        sharePhoto = SharePhoto.Builder()
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)
        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result) {
                //Log.d(TAG, result.getPostId());


            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                error.printStackTrace()

            }
        })
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage(getString(R.string.loading))
        ProductViewPresenter(this, NetModule.provideRetrofit())
        vistStore.setOnClickListener {
            val i = Intent(this@ProductViewActivity, StoreActivity::class.java)
            val storeModel = StoreModel()
            storeModel.slug = productModel.store.slug
            storeModel.name = productModel.store.name
            storeModel.logo = productModel.store.logo
            i.putExtra("data", storeModel)
            startActivity(i)
        }

        map_layout.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + productModel.location.coordinates[0] + "," + productModel.location.coordinates[1]))
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


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        shareProgress.visibility = View.GONE
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    @Throws(MalformedURLException::class)
    fun downloadImage(Imageurl: String) {
        runOnUiThread {
            AppEventsLogger.logItemDownloadEvent(productModel.name, productModel.store.name, productModel.category)
            Toast.makeText(this@ProductViewActivity, "Saving image.......", Toast.LENGTH_LONG).show()
        }
    }

    private fun initUi() {
        val robotMedium = Typeface.createFromAsset(assets,
                "fonts/Roboto-Medium.ttf")
        val robotCondensed = Typeface.createFromAsset(assets,
                "fonts/RobotoCondensed-Regular.ttf")
        val robotBold = Typeface.createFromAsset(assets,
                "fonts/RobotoCondensed-Bold.ttf")
        val robotThinItalic = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
        if (productModel.location != null) {
            coord = LatLng(productModel.location.coordinates[0], productModel.location.coordinates[1])
        }
        productPrice.typeface = robotMedium
        productName.typeface = robotMedium
        productDescription.typeface = robotThinItalic
        productName.text = productModel.name
        productDescription.text = productModel.description
        productPrice.setText(productModel.price)
        storeName.text = productModel.store.name
        val imageByte = Base64.decode(productModel.image.placeholder, Base64.DEFAULT)
        val bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
        productImage.setImageBitmap(bit)
        content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://shop440.com/products/" + productModel.slug))
                .setContentTitle(productModel.name)
                .setImageUrl(Uri.parse(productModel.image.path))
                .build()
        // SharePhoto.Builder sharePhoto = new SharePhoto.Builder();
        sharePhoto.setImageUrl(Uri.parse(productModel.image.path))
        val shareCard = findViewById(R.id.shareCard) as CardView
        val shareContent = ShareMediaContent.Builder().addMedium(sharePhoto.build()).build()
        shareCard.setOnClickListener {
            AppEventsLogger.logItemShareEvent()
            shareProgress.visibility = View.VISIBLE
            shareDialog.show(shareContent)
        }
        productImage.setImageUrl(productModel.image.path, imageLoader)
        showNativeAd()
        progressDialog.show()

        val handler = Handler()
        handler.postDelayed({
            map.onCreate(bundle)
            map.getMapAsync(productView)
        }, 1800)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("here", "here")
        val builder = LatLngBounds.builder()
        builder.include(coord)
        googleMap.addMarker(MarkerOptions().position(coord).title(productModel.store.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15f))
        map.onResume()
        progressDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.productview, menu)
        //MenuItem searchItem = menu.findItem(R.id.search);

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.download) {
            try {
                presenter.downloadImage(productModel.image.path, productModel.name, fileCache)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        if (!data.isEmpty()) {
            val resolvedUrl = data.substring(data.lastIndexOf("/") + 1)
            presenter.loadData(resolvedUrl)
        } else {
            productModel = intent.getSerializableExtra("data") as Datum
            initUi()
        }
    }

    private fun showNativeAd() {
        nativeAd = NativeAd(this, "909211035848244_1018000754969271")
        nativeAd.setAdListener(object : AdListener {

            override fun onError(ad: Ad, error: AdError) {
                // Ad error callback
            }

            override fun onAdLoaded(ad: Ad) {
                    nativeAd.unregisterView()
                // Add the Ad view into the ad container.
                val nativeAdContainer = findViewById(R.id.native_ad_container) as LinearLayout
                val inflater = LayoutInflater.from(this@ProductViewActivity)
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                val adView = inflater.inflate(R.layout.ad_layout, nativeAdContainer, false)
                nativeAdContainer.addView(adView)

                // Create native UI using the ad metadata.
                val nativeAdIcon = adView.findViewById(R.id.native_ad_icon) as ImageView
                val nativeAdTitle = adView.findViewById(R.id.native_ad_title) as TextView
                val nativeAdMedia = adView.findViewById(R.id.native_ad_media) as MediaView
                val nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context) as TextView
                val nativeAdBody = adView.findViewById(R.id.native_ad_body) as TextView
                val nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action) as Button

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
                val adChoicesContainer = findViewById(R.id.ad_choices_container) as LinearLayout
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

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun showProduct(product: Datum) {
        this@ProductViewActivity.productModel = product
        initUi()
    }

    override fun imageDownloaded(filePath: File) {
        runOnUiThread { Toast.makeText(this@ProductViewActivity, "Product Image saved to shop440" + " " + filePath.absolutePath, Toast.LENGTH_LONG).show() }
    }
}
