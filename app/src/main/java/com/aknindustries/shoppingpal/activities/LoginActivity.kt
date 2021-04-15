package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreAuthClass
import com.google.firebase.auth.FirebaseAuth
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
                showProgressDialog("Logging in")
                val email = findViewById<TextView>(R.id.et_email_id).text.toString().trim()
                val password = findViewById<TextView>(R.id.et_password).text.toString().trim()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FireStoreAuthClass().getCurrentUser(this)
                    } else {
                        showSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }

        // Set on click listener on forgot password
        tv_forgot_password.setOnClickListener {
            intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
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

    fun loginSuccess() {
        showSnackBar("Successfully registered!", false)
        intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}