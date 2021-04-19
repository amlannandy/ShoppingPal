package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreAuthClass
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUser : User
    private var mImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mUser = User()

        getExistingUserDetails()
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_user_profile_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getExistingUserDetails() {
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.setText(mUser.firstName)
        et_last_name.setText(mUser.lastName)
        et_email.setText(mUser.email)
        et_email.isEnabled = false

        if (mUser.profileCompleted) {
            setupActionBar()
            tv_title.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this).loadUserImage(mUser.imageUrl!!, iv_user_photo)
            et_mobile_number.setText(mUser.phone)
            if (mUser.gender == Constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        } else {
            et_first_name.isEnabled = false
            et_last_name.isEnabled = false
        }

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
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if (!mUser.profileCompleted) {
                            FireStoreAuthClass().uploadImageToCloudStorage(this, mImageUri!!)
                        } else {
                            if (mImageUri != null) {
                                FireStoreAuthClass().uploadImageToCloudStorage(this, mImageUri!!)
                            }
                            else {
                                imageUploadSuccess(null)
                            }
                        }
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
                        mImageUri = data.data!!
                        GlideLoader(this).loadUserImage(mImageUri!!, iv_user_photo)
                    } catch (e : IOException) {
                        e.printStackTrace()
                        showSnackBar(resources.getString(R.string.image_selection_failure), true)
                    }
                }
            }
        }
    }

    private fun validateCompleteProfile() : Boolean {
        val firstName = et_first_name.text.toString().trim()
        val lastName = et_last_name.text.toString().trim()
        val email = et_email.text.toString().trim()
        val phone : String = et_mobile_number.text.toString().trim()
        return when {
            TextUtils.isEmpty(firstName) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                return false
            }
            TextUtils.isEmpty(lastName) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                return false
            }
            TextUtils.isEmpty(email) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                return false
            }
            TextUtils.isEmpty(phone) -> {
                showSnackBar(resources.getString(R.string.enter_phone_number), true)
                return false
            }
            // Check for valid email
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_valid_email), true)
                false
            }
            phone.length != 10 -> {
                showSnackBar(resources.getString(R.string.invalid_phone_number), true)
                return false
            }
            mImageUri == null && !mUser.profileCompleted -> {
                showSnackBar(resources.getString(R.string.no_profile_image), true)
                return false
            }
            else -> true
        }
    }

    fun completeProfileSuccess() {
        showSnackBar(resources.getString(R.string.complete_profile_success), false)
        if (mUser.profileCompleted) {
            intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            intent = Intent(this@ProfileActivity, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun imageUploadSuccess(imageURL : String?) {
        val userHashMap = HashMap<String, Any>()
        val firstName = et_first_name.text.toString().trim()
        val lastName = et_last_name.text.toString().trim()
        val email = et_email.text.toString().trim()
        val phone = et_mobile_number.text.toString().trim()
        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        userHashMap[Constants.PHONE] = phone
        userHashMap[Constants.GENDER] = gender
        if (imageURL != null) {
            userHashMap[Constants.IMAGE_URL] = imageURL
        }
        if (mUser.profileCompleted) {
            userHashMap[Constants.FIRST_NAME] = firstName
            userHashMap[Constants.LAST_NAME] = lastName
            userHashMap[Constants.EMAIL] = email
        }
        userHashMap[Constants.PROFILE_COMPLETED] = true
        FireStoreAuthClass().completeUserRegistration(this, userHashMap)
    }
}