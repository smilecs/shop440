package com.shop440.navigation.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.navigation.MainNavigation
import com.shop440.utils.Image
import com.shop440.utils.PreferenceManager
import kotlinx.android.synthetic.main.profile_scene.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileSessionFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_scene, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews(){
        PreferenceManager.PrefData.getPreferenceManager()?.apply {
            profileNameText.text = name
            profilePhoneText.text = phone
            val base64 = image
            if (base64 != null && !base64.isEmpty()) {
                profileImageView.setImageDrawable(Image.roundedBitmapDrawable(context, Image.base64ToBitmap(base64)))
            }
        }

        logOut.setOnClickListener {
            PreferenceManager.PrefData.getPreferenceManager()?.clearPreference()
            (activity as MainNavigation).switch()
        }

    }
}