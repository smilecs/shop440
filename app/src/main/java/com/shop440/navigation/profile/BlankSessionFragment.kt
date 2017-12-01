package com.shop440.navigation.profile


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shop440.R
import com.shop440.auth.AuthActivity
import com.shop440.navigation.MainNavigation
import kotlinx.android.synthetic.main.profile_no_token.*


/**
 * A simple [Fragment] subclass.
 * Use the [BlankSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankSessionFragment : Fragment() {
    private val FROM_PROFILE = 2005

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_no_token, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        authButtonProfile.setOnClickListener({
            val intent = Intent(context, AuthActivity::class.java)
            startActivityForResult(intent, FROM_PROFILE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
            (activity as MainNavigation).switch()
        }
    }

}// Required empty public constructor
