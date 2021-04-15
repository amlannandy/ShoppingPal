package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreAuthClass
import com.aknindustries.shoppingpal.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
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
        val loginText : TextView = findViewById(R.id.tv_login)
        loginText.setOnClickListener {
            onBackPressed()
        }

        // Set on click listen on register button
        val registerButton: Button = findViewById(R.id.btn_register)
        registerButton.setOnClickListener{
            if (validateRegistration()) {
                showProgressDialog("Registering user")
                val firstName = et_first_name.text.toString().trim()
                val lastName = et_last_name.text.toString().trim()
                val email = et_email.text.toString().trim()
                val password = et_password.text.toString().trim()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!
                        val user = User(
                            id = firebaseUser.uid,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                        )
                        FireStoreAuthClass().registerUser(this, user)
                    } else {
                        hideProgressDialog()
                        showSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }

    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegistration() :Boolean {
        val firstName = findViewById<TextView>(R.id.et_first_name).text.toString().trim()
        val lastName = findViewById<TextView>(R.id.et_last_name).text.toString().trim()
        val email = findViewById<TextView>(R.id.et_email).text.toString().trim()
        val password = findViewById<TextView>(R.id.et_password).text.toString().trim()
        val confirmPassword = findViewById<TextView>(R.id.et_confirm_password).text.toString().trim()
        val termsAndConditions :Boolean = findViewById<CheckBox>(R.id.cb_terms_and_condition).isChecked
        return when {
            TextUtils.isEmpty(firstName) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(lastName) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(email) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            // Check for valid email
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_valid_email), true)
                false
            }

            password != confirmPassword -> {
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

    fun registrationSuccess() {
        hideProgressDialog()
        showSnackBar("Successfully registered!", false)
        intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun registrationFailure(errorMessage : String) {
        hideProgressDialog()
        showSnackBar(errorMessage, true)
    }
}