package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.aknindustries.shoppingpal.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Set on click listener of Register button
        tv_register.setOnClickListener {
            intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Set on click listen on Login Button
        btn_login.setOnClickListener {
            if (validateLogin()) {
                showSnackBar("TEst success", false)
            }
        }
    }

    private fun validateLogin() : Boolean {
        val email = findViewById<TextView>(R.id.et_email_id).text.toString().trim()
        val password = findViewById<TextView>(R.id.et_password).text.toString().trim()
        return when {
            TextUtils.isEmpty(email) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_password), true)
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