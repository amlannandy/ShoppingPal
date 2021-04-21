package com.aknindustries.shoppingpal.fragments

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.aknindustries.shoppingpal.R
import kotlinx.android.synthetic.main.progress_indicator.*

open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog: Dialog

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.progress_indicator)

        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}