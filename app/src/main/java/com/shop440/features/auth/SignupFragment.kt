package com.shop440.features.auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bentech.android.appcommons.utils.EditTextUtils
import com.bentech.android.appcommons.validator.EditTextEmailValidator
import com.bentech.android.appcommons.validator.EditTextPhoneNumberValidator
import com.bentech.android.appcommons.validator.EditTextRequiredInputValidator
import com.shop440.repository.dao.models.User
import com.shop440.R
import com.shop440.repository.api.NetModule
import com.shop440.utils.Image
import com.shop440.utils.ProgressHelper
import kotlinx.android.synthetic.main.signup.*
import java.io.IOException

class SignupFragment : Fragment(), AuthContract.View {
    private val PICK_IMAGE_REQUEST = 1

    override lateinit var presenter: AuthContract.Presenter
    private var progressDialog: ProgressDialog? = null
    private var bitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.signup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        progressDialog = ProgressHelper.progressDialog(context)
        presenter = AuthPresenter(this, NetModule.provideRetrofit())
        userImageContainerSignUp.setOnClickListener {
            addProfilePicture()
        }

        arguments?.let {
            providerLogin(it.getSerializable("data") as User)
        }

        phoneSignUp.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        signupButton.setOnClickListener {
            if (EditTextUtils.isInValid(EditTextRequiredInputValidator(nameSignUp),
                    EditTextPhoneNumberValidator(phoneSignUp),
                    EditTextEmailValidator(emailSignUp), EditTextRequiredInputValidator(passwordSignUp),
                    EditTextRequiredInputValidator(passwordReSignUp))) {
                return@setOnClickListener
            }
            if (passwordReSignUp.text.toString() != passwordSignUp.text.toString()) {
                passwordReSignUp.error = getString(R.string.password_match_error)
                return@setOnClickListener
            }
            val user = User()
            user.name = nameSignUp.text.toString()
            user.phone = phoneSignUp.text.toString().apply {
                replace("+", "")
                replace(" ", "")
                replace("234", "0")
                replace("(", "")
                replace(")", "")
                replace("-", "")
            }
            Log.i("phone", user.phone)
            user.email = emailSignUp.text.toString()
            user.password = passwordSignUp.text.toString()
            user.image = Image.base64String(bitmap)
            presenter.checkExisting(user)
        }
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        if (progressDialog?.isShowing!!) {
            progressDialog?.dismiss()
        } else {
            progressDialog?.show()
        }

    }

    override fun saveUser(user: User) {
        val intent = Intent(context, VerifyActivity::class.java)
        intent.putExtra("data", user)
        activity.startActivityForResult(intent, 200)
    }

    fun addProfilePicture() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                imageView.setImageBitmap(bitmap)

            } catch (error: IOException) {

            } catch (error: NullPointerException) {

            }
        }
    }

    fun providerLogin(user: User) {
        nameSignUp.setText(user.name)
        phoneSignUp.setText(user.phone)
        emailSignUp.setText(user.email)
        user.image = Image.base64String(bitmap)
    }
}
