package com.aknindustries.shoppingpal.firebase

import com.aknindustries.shoppingpal.activities.RegisterActivity
import com.aknindustries.shoppingpal.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStoreClass = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, user: User) {
        mFireStoreClass.collection("users").document(user.id)
            .set(user, SetOptions.merge()).addOnSuccessListener {
                activity.registrationSuccess()
        }.addOnFailureListener { exception ->
            activity.registrationFailure(exception.message.toString())
        }
    }

}