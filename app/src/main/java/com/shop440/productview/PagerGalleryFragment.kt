package com.shop440.productview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide

import com.shop440.R
import kotlinx.android.synthetic.main.image_gallery.*


/**
 * A simple [Fragment] subclass.
 * Use the [PagerGalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagerGalleryFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.image_gallery, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mParam1?.let {
            Glide.with(context)
                    .asBitmap()
                    .load("https://tinyfiles.past3dev.com/resize?width=800&height=0&type=jpeg&nocrop=true&url="+it)
                    .into(imageGalleryPreview)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment PagerGalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?): PagerGalleryFragment {
            val fragment = PagerGalleryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
