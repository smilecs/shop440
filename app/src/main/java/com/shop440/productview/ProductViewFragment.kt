package com.shop440.productview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shop440.R


/**
 * A simple [Fragment] subclass.
 * Use the [ProductViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductViewFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false)
    }

    companion object {

        fun newInstance(param1: String, param2: String): ProductViewFragment {
            val fragment = ProductViewFragment()
            val args = Bundle()
            //args.putString(ARG_PARAM1, param1)
            //args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}
