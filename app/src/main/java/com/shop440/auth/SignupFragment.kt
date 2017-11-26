package com.shop440.auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bentech.android.appcommons.utils.EditTextUtils
import com.bentech.android.appcommons.validator.EditTextEmailValidator
import com.bentech.android.appcommons.validator.EditTextPhoneNumberValidator
import com.bentech.android.appcommons.validator.EditTextRequiredInputValidator
import com.shop440.models.User
import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.utils.Image
import com.shop440.utils.ProgressHelper
import kotlinx.android.synthetic.main.signup.*
import java.io.IOException

class SignupFragment : Fragment(), AuthContract.View {
    private val PICK_IMAGE_REQUEST = 1
    private val TO_VERIFY = 200

    override lateinit var presenter: AuthContract.Presenter
    private var progressDialog:ProgressDialog? = null
    private var bitmap:Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?):View?{
        return inflater?.inflate(R.layout.signup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        progressDialog = ProgressHelper.progressDialog(context)
        userImageContainerSignUp.setOnClickListener{
            addProfilePicture()
        }
        phoneSignUp.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        signupButton.setOnClickListener {
            if(EditTextUtils.isInValid(EditTextRequiredInputValidator(nameSignUp),
                    EditTextPhoneNumberValidator(phoneSignUp), EditTextEmailValidator(emailSignUp), EditTextRequiredInputValidator(passwordSignUp))){
                return@setOnClickListener
            }
            val user = User()
            user.name = nameSignUp.text.toString()
            user.phone = phoneSignUp.text.toString().replace(" ", "")
            user.email = emailSignUp.text.toString()
            user.password = passwordSignUp.text.toString()
            user.image = Image.base64String(bitmap)
            saveUser(user)
        }
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        if(progressDialog?.isShowing!!){
            progressDialog?.dismiss()
        }else{
            progressDialog?.show()
        }

    }

    override fun saveUser(user: User) {
        val intent = Intent(context, VerifyActivity::class.java)
        intent.putExtra("data", user)
        startActivityForResult(intent, TO_VERIFY)
    }

    fun addProfilePicture(){
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            val uri = data?.data
            bitmap = try{
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }catch (error: IOException){
                null
            }catch (error: NullPointerException){
                null
            }
            imageView.setImageBitmap(bitmap)
        }
    }
}
