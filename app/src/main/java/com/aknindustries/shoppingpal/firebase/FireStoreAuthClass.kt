package com.aknindustries.shoppingpal.firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.aknindustries.shoppingpal.activities.LoginActivity
import com.aknindustries.shoppingpal.activities.ProfileActivity
import com.aknindustries.shoppingpal.activities.RegisterActivity
import com.aknindustries.shoppingpal.activities.SplashActivity
import com.aknindustries.shoppingpal.models.User
import com.aknindustries.shoppingpal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.lang.NullPointerException

class FireStoreAuthClass {

    private val mFireStoreClass = FirebaseFirestore.getInstance()
    private val mFirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(activity: RegisterActivity, user: User) {
        mFireStoreClass.collection(Constants.USERS).document(user.id)
            .set(user, SetOptions.merge()).addOnSuccessListener {
                saveUserLocally(activity, user)
                activity.registrationSuccess()
        }.addOnFailureListener { exception ->
            activity.registrationFailure(exception.message.toString())
        }
    }

    fun completeUserRegistration(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStoreClass.collection(Constants.USERS).document(getCurrentUserId()!!)
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                        activity.completeProfileSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                        activity.showSnackBar(e.message.toString(), true)
                    }
                }
            }
    }

    fun getCurrentUserId() : String? {
        return try {
            mFirebaseAuth.currentUser!!.uid
        } catch (e : NullPointerException) {
            null
        }
    }

    fun getCurrentUser(activity: Activity) {
        var userId = ""
        try {
            userId = mFirebaseAuth.currentUser!!.uid
        } catch (e: NullPointerException) {
            when (activity) {
                is SplashActivity -> {
                    activity.loadLoginScreen()
                    return
                }
            }
        }
        mFireStoreClass.collection(Constants.USERS).document(userId).get().addOnSuccessListener { doc ->
            val user = doc.toObject(User::class.java)!!

            // Save username to shared preferences
            saveUserLocally(activity, user)

            when (activity) {
                is SplashActivity -> {
                    if (user.profileCompleted)
                        activity.loadHomeScreen()
                    else
                        activity.loadProfileScreen(user)
                }
                is LoginActivity -> {
                    activity.loginSuccess()
                }
            }
        }.addOnFailureListener{
            when (activity) {
                is SplashActivity -> {
                    activity.loadLoginScreen()
                }
            }
        }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageUri: Uri) {
        val sRef = FirebaseStorage.getInstance().reference.child(
            Constants.PROFILE_PICTURE_FOLDER +  "/" + getCurrentUserId() + "/" +
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + Constants.getFileExtension(activity, imageUri)
        )
        sRef.putFile(imageUri)
            .addOnSuccessListener { taskScreenshot ->
                taskScreenshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { imageUri ->
                        when (activity) {
                            is ProfileActivity -> {
                                activity.imageUploadSuccess(imageUri.toString())
                            }
                        }
                    }
            }.addOnFailureListener { e ->
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                        activity.showSnackBar(e.message.toString(), true)
                    }
                }
            }

    }

    private fun saveUserLocally(activity: Activity, user: User) {
        val sharedPreferences = activity.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
        editor.apply()
    }

}