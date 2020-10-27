package com.albertomoya.readchat.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.isValidEmail
import com.albertomoya.readchat.others.onChange
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.others.toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import kotlinx.android.synthetic.main.dialog_forgot_password.view.*

class ForgotPasswordDialog: DialogFragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //setUpCurrentUser()
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        view.textInputForgotPasswordEmail.onChange {
            view.textInputForgotPasswordEmail.error = if (isValidEmail(it)) null else getString(R.string.error_email_is_not_valid)
        }


        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.title_dialog_forgot_password))
            .setView(view)
            .setPositiveButton(getString(R.string.button_ok)){_,_ ->
                val email = view.textInputForgotPasswordEmail.text.toString()
                if (email.isNotEmpty()){
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener{
                        
                    }
                } else {
                    activity!!.toast("sadasdad")
                }
            }
            .setNegativeButton(getString(R.string.button_cancel)){_,_ ->

            }
            .create()
    }

}




