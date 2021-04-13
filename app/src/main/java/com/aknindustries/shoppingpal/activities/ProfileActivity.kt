package com.aknindustries.shoppingpal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        getExistingUserDetails()
    }

    private fun getExistingUserDetails() {
        var user = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        et_first_name.isEnabled = false
        et_first_name.setText(user.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(user.lastName)

        et_email.isEnabled = false
        et_email.setText(user.email)

    }
}