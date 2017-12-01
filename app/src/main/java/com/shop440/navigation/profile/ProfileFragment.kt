package com.shop440.navigation.profile

import android.content.Intent
import android.os.Bundle
import android.support.transition.Scene
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.shop440.R
import com.shop440.auth.AuthActivity
import com.shop440.utils.Image
import com.shop440.utils.PreferenceManager
import kotlinx.android.synthetic.main.profile_container.*

/**
 * Created by mmumene on 26/11/2017.
 */
class ProfileFragment : Fragment() {
    private val FROM_PROFILE = 2005
    private val userToken = PreferenceManager.PrefData.getPreferenceManager()?.getSavedToken()
    private val userName = PreferenceManager.PrefData.getPreferenceManager()?.getSavedName()
    private val emptySessionScene: Scene by lazy {
        Scene.getSceneForLayout(sceneRoot, R.layout.profile_no_token, context)
    }
    private val profileScene: Scene by lazy {
        Scene.getSceneForLayout(sceneRoot, R.layout.profile_scene, context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.profile_container, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmSession()
    }

    private fun sceneForEmptySession(): Runnable {
        return Runnable {
            val authButton = view?.findViewById(R.id.authButtonProfile)
            authButton?.setOnClickListener({
                val intent = Intent(context, AuthActivity::class.java)
                startActivityForResult(intent, FROM_PROFILE)
            })
        }

    }

    private fun sceneForProfile(): Runnable {
        return Runnable {
            val profileName = view?.findViewById(R.id.profileNameText) as TextView
            val phoneText = view?.findViewById(R.id.profilePhoneText) as TextView
            val profileImage = view?.findViewById(R.id.profileImageView) as ImageView
            PreferenceManager.PrefData.getPreferenceManager()?.apply {
                profileName.text = getSavedName()
                phoneText.text = getSavedPhone()
                val base64 = getSavedImage()
                if (base64 != null && !base64.isEmpty()) {
                    profileImage.setImageBitmap(Image.base64ToBitmap(base64))
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
        confirmSession()

    }

    private fun confirmSession() {
        val scene = if (userToken.isNullOrEmpty() && userName.isNullOrEmpty()) {
            emptySessionScene.setEnterAction { sceneForEmptySession().run() }
            emptySessionScene
        } else {
            profileScene.setEnterAction { sceneForProfile().run() }
            profileScene
        }
        scene.enter()
    }
}