package com.shop440.navigation.profile

import android.content.Intent
import android.os.Bundle
import android.support.transition.Scene
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.auth.AuthActivity
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.profile_container, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userToken.isNullOrEmpty() && userName.isNullOrEmpty()) {
            emptySessionScene.setEnterAction { sceneForEmptySession().run() }
            TransitionManager.go(emptySessionScene)
        }
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
}