package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreClass
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mUser = User()

        getExistingUserDetails()
    }

    private fun getExistingUserDetails() {
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        et_first_name.isEnabled = false
        et_first_name.setText(mUser.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(mUser.lastName)

        et_email.isEnabled = false
        et_email.setText(mUser.email)

        iv_user_photo.setOnClickListener(this@ProfileActivity)
        btn_submit.setOnClickListener(this@ProfileActivity)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit -> {
                    if (validateCompleteProfile()) {
                        val userHashMap = HashMap<String, Any>()
                        val phone = et_mobile_number.text.toString().trim()
                        val gender = if (rb_male.isChecked) {
                            Constants.MALE
                        } else {
                            Constants.FEMALE
                        }
                        userHashMap[Constants.PHONE] = phone
                        userHashMap[Constants.GENDER] = gender
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FireStoreClass().completeUserRegistration(this, userHashMap)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                showSnackBar(resources.getString(R.string.read_storage_permission_denied), false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val imageUri = data.data!!
                        GlideLoader(this).loadUserImage(imageUri, iv_user_photo)
                    } catch (e : IOException) {
                        e.printStackTrace()
                        showSnackBar(resources.getString(R.string.image_selection_failure), true)
                    }
                }
            }
        }
    }

    private fun validateCompleteProfile() : Boolean {
        val phone : String = et_mobile_number.text.toString().trim()
        return when {
            TextUtils.isEmpty(phone) -> {
                showSnackBar(resources.getString(R.string.enter_phone_number), true)
                return false
            }
            phone.length != 10 -> {
                showSnackBar(resources.getString(R.string.invalid_phone_number), true)
                return false
            }
            else -> true
        }
    }

    fun completeProfileSuccess() {
        showSnackBar(resources.getString(R.string.complete_profile_success), false)
        intent = Intent(this@ProfileActivity, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}