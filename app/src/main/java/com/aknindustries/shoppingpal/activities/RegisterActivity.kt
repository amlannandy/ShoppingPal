package com.aknindustries.shoppingpal.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.aknindustries.shoppingpal.R

class RegisterActivity : SnackBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        // Set on click listener of login button
        val loginText : TextView = findViewById<TextView>(R.id.tv_login)
        loginText.setOnClickListener {
            onBackPressed()
        }

        // Set on click listen on register button
        val registerButton: Button = findViewById(R.id.btn_register)
        registerButton.setOnClickListener{
            if (validateRegistration())
                showSnackBar("Registration successful", false)
        }

    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegistration() :Boolean {
        val firstName = findViewById<TextView>(R.id.et_first_name).text.toString()
        val lastName = findViewById<TextView>(R.id.et_last_name).text.toString()
        val email = findViewById<TextView>(R.id.et_email).text.toString()
        val password = findViewById<TextView>(R.id.et_password).text.toString()
        val confirmPassword = findViewById<TextView>(R.id.et_confirm_password).text.toString()
        val termsAndConditions :Boolean = findViewById<CheckBox>(R.id.cb_terms_and_condition).isChecked
        return when {
            TextUtils.isEmpty(firstName.trim()) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(lastName.trim()) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(email.trim()) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password.trim()) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(confirmPassword.trim()) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            // Check for valid email
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_valid_email), true)
                false
            }

            password.trim() != confirmPassword.trim() -> {
                showSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !termsAndConditions -> {
                showSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> true
        }
    }
}