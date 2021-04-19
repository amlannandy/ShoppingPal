package com.aknindustries.shoppingpal.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreAuthClass
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.iv_user_photo

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()
        getUserDetails()

        tv_edit.setOnClickListener(this@SettingsActivity)
        btn_logout.setOnClickListener(this@SettingsActivity)

    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreAuthClass().getCurrentUser(this)
    }

    @SuppressLint("SetTextI18n")
    fun getUserDetailsSuccess(user: User) {
        hideProgressDialog()
        mUserDetails = user
        GlideLoader(this).loadUserImage(user.imageUrl!!, iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_gender.text = user.gender
        tv_mobile_number.text = user.phone
    }

    fun getUserDetailsFailure() {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.get_user_details_error), true)
    }

    fun logOutSuccess() {
        intent = Intent(this@SettingsActivity, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout -> {
                    FireStoreAuthClass().logOut(this)
                }
                R.id.tv_edit -> {
                    intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
            }
        }
    }

}