package com.aknindustries.shoppingpal.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(), View.OnClickListener {
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

        iv_user_photo.setOnClickListener(this@ProfileActivity)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar("The storage permission is granted.", false)
            } else {
                showSnackBar(resources.getString(R.string.read_storage_permission_denied), false)
            }
        }
    }
}