package com.aknindustries.shoppingpal.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreAuthClass
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()
        getUserDetails()

        btn_logout.setOnClickListener{ FireStoreAuthClass().logOut(this) }
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

}