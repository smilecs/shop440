package com.shop440.productview


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import com.shop440.R
import com.shop440.models.Image
import com.shop440.models.ProductFeed
import kotlinx.android.synthetic.main.fragment_product_view.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProductViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductViewFragment : AppCompatActivity(), OnMapReadyCallback {
  lateinit var productFeed:ProductFeed


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        productFeed.images?.let {
            imagePager.adapter = ViewAdapter(this@ProductViewFragment, it)
        }

    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    companion object {
        fun newInstance(productFeed: ProductFeed): ProductViewFragment {
            val fragment = ProductViewFragment()
            fragment.productFeed = productFeed
            return fragment
        }
    }

     internal class ViewAdapter(val context: Context, val image: List<Image>) : PagerAdapter(){

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val layoutInflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.image_gallery, container, false)
            val imageView = view.findViewById<ImageView>(R.id.imageGalleryPreview)
            val imageAtPosition = image[position]
            Glide.with(context)
                    .load(imageAtPosition.url)
                    .thumbnail(0.5f)
                    .into(imageView)
            container?.addView(imageView)

            return view
        }

        override fun isViewFromObject(view: View?, obj: Any?): Boolean {
            return view == obj as View
        }

        override fun getCount(): Int = image.size

         override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
             container?.removeView(`object` as View)
         }
     }

}
