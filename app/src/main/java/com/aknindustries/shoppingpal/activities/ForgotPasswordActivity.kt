package com.aknindustries.shoppingpal.activities

import android.os.Bundle
import android.text.TextUtils
import com.aknindustries.shoppingpal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()

        btn_send_mail.setOnClickListener {
            val email : String = et_email.text.toString().trim()
            if (validateForgotPassword(email)) {
                showProgressDialog("Trying to send mail...")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        showSnackBar("Success! Please check your email",false)
                    } else {
                        showSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }
    }

    private fun setupActionBar() {
        val toolbar = toolbar_forgot_password_activity
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateForgotPassword(email : String) : Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            // Check for valid email
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_valid_email), true)
                false
            }
            else -> true
        }
    }
}