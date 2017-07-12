package com.shop440

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.volley.*
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.shop440.Adapters.ProductAdapter
import com.shop440.Models.ProductModel
import com.shop440.Models.StoreModel
import com.shop440.Utils.EndlessRecyclerViewScrollListener
import com.shop440.Utils.Metrics
import com.shop440.Utils.Urls
import com.shop440.Utils.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlinx.android.synthetic.main.store_header.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.activity_store.*


class StoreActivity : AppCompatActivity() {
    lateinit var mainAdapter: ProductAdapter
    lateinit var model: ArrayList<ProductModel>
    var TAG = "StoreModel"
    lateinit var volleySingleton: VolleySingleton
    lateinit var requestQueue: RequestQueue
    lateinit var sharedPreferences: SharedPreferences
    lateinit var token: String
    lateinit var layoutManager: StaggeredGridLayoutManager
    lateinit var refreshLayout: SwipeRefreshLayout
    var next: Boolean = true
    lateinit var store: StoreModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        store = intent.getSerializableExtra("data") as StoreModel
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(store.name)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        sharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        token = sharedPreferences.getString(Urls.TOKEN, "null")
        if (intent.getBooleanExtra("reload", false)) {
            Get_Store()
        }
        store_header_name.text = store.name
        store_header_description.text = store.description
        model = ArrayList<ProductModel>()
        mainAdapter = ProductAdapter(this, model)
        refreshLayout = findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )

        refreshLayout.setOnRefreshListener {
            GetData("1")
        }
        layoutManager = StaggeredGridLayoutManager(Metrics.GetMetrics(recyclerView, this), StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mainAdapter
        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (next) {
                    GetData(page.toString())
                }
            }
        })


        fab.visibility = View.GONE
        fab.setOnClickListener {
            val i = Intent(this@StoreActivity, NewItemCategoryActivity::class.java)
            //i.putExtra("data", productModel);
            i.putExtra("backtrack", store)
            startActivity(i)
        }

        volleySingleton = VolleySingleton.getsInstance()
        requestQueue = volleySingleton.getmRequestQueue()
        profile.setImageDrawable(resources.getDrawable(R.drawable.ic_online_store))
        volleySingleton.imageLoader.get(store.logo, object : ImageLoader.ImageListener {
            override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                if (response.bitmap != null) {
                    profile.setImageBitmap(response.bitmap)
                }
            }

            override fun onErrorResponse(error: VolleyError) {
                error.printStackTrace()
            }
        })


        GetData("1")
        Get_Store()
    }

    private fun GetData(page: String) {
        if (page == "1") {
            model.clear()
        }
        refreshLayout.isRefreshing = true
        feedback.visibility = View.GONE
        val jsonArrayRequest = JsonObjectRequest(Urls.BASE_URL + Urls.GETSTOREPRODUCTS + store.slug + "/products?p=" + page, null, Response.Listener<JSONObject> { response ->
            try {
                refreshLayout.isRefreshing = false
                val array = response.getJSONArray("Data")
                next = response.getJSONObject("Page").getBoolean("Next")
                for (i in 0..array.length() - 1) {
                    val obj = array.getJSONObject(i)
                    val product = ProductModel()
                    product.name = obj.getString("Name")
                    product.slug = obj.getString("Slug")
                    product.description = obj.getString("Description")
                    product.price = obj.getString("Price")
                    product.category = obj.getString("Category")
                    product.city = obj.getString("City")
                    product.citySlug = obj.getString("CitySlug")
                    product.owner = obj.getJSONObject("Store").getString("Name")
                    product.ownerSlug = obj.getJSONObject("Store").getString("Slug")
                    product.ownerLogo = obj.getJSONObject("Store").getString("Logo")
                    product.specialisation = obj.getJSONObject("Store").getString("Specialisation")
                    product.coordinates = obj.getJSONObject("Location").getJSONArray("Coordinates").getString(0) + "," + obj.getJSONObject("Location").getJSONArray("Coordinates").getString(1)
                    product.image = obj.getJSONObject("Image").getString("Path")
                    val placeholder = obj.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    try {
                        product.placeholder = placeholder[1]
                    } catch (e: Exception) {
                        e.printStackTrace()
                        product.placeholder = ""
                    }

                    model.add(product)
                }
                mainAdapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            feedback!!.visibility = View.VISIBLE
            //Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_SHORT);
        })
        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestQueue.add(jsonArrayRequest)
    }

    public override fun onResume() {
        super.onResume()
        //GetData("1");
    }

    fun Get_Store() {
        Log.d(TAG, Urls.BASE_URL + Urls.SINGLESTORE)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.SINGLESTORE + store.slug, null, Response.Listener<JSONObject> { response ->
            try {
                Log.d(TAG, response.toString())
                //progressBar.setVisibility(View.GONE);
                //JSONArray jsonArray = response.getJSONArray("Stores");
                val store = StoreModel()
                store.name = response.getString("Name")
                store.slug = response.getString("Slug")
                store.description = response.getString("Description")
                store.productsNumber = response.getJSONObject("Analytics").getString("Products")
                store.purchases = response.getJSONObject("Analytics").getString("Purchases")
                store.likes = response.getJSONObject("Analytics").getString("Likes")
                store.logo = response.getString("Logo")
                store_header_name.text = store.name
                store_header_description.text = store.description
                //model.add(store);
                //storeAdapter.notifyDataSetChanged();
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            Log.d(TAG, "bad url")
        })
        requestQueue.add(jsonObjectRequest)
    }

}
