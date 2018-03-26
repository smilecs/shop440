package com.shop440.features.checkout

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bentech.android.appcommons.utils.EditTextUtils
import com.bentech.android.appcommons.validator.EditTextRequiredInputValidator
import com.shop440.R
import com.shop440.repository.dao.models.UserAdress
import kotlinx.android.synthetic.main.address_layout.*


class AddressSheetFragment : BottomSheetDialogFragment() {

    private lateinit var mListener: OnFragmentAddressListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.address_layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveButton.setOnClickListener {
            if(EditTextUtils.isInValid(EditTextRequiredInputValidator(addressTextView), EditTextRequiredInputValidator(cityTextView))){
                return@setOnClickListener
            }
            val userAddress = UserAdress().apply {
                address = addressTextView.text.toString()
                city = cityTextView.text.toString()

            }
            mListener.onFragmentInteraction(userAddress)
            dismiss()
        }
    }

    interface OnFragmentAddressListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(userAdress: UserAdress)
    }

    companion object {

        fun newInstance(fragmentAddress:OnFragmentAddressListener): AddressSheetFragment {
            val fragment = AddressSheetFragment()
            fragment.mListener = fragmentAddress
            return fragment
        }
    }
}
