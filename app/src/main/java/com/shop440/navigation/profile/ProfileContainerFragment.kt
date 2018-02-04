package com.shop440.navigation.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.utils.PreferenceManager


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileContainerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_container, container, false)
    }

    override fun onResume() {
        super.onResume()
        delegateView()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        delegateView()
    }

    private fun delegateView(){
        val fragment = if(!PreferenceManager.PrefData.getPreferenceManager()?.token.isNullOrBlank()){
            ProfileSessionFragment()
        }else{
            BlankSessionFragment()
        }
        childFragmentManager.beginTransaction().apply {
            replace(R.id.sceneRoot, fragment, "profile")
        }.commit()
    }
}
