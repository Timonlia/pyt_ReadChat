package com.albertomoya.readchat.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.login.SignInActivity
import com.albertomoya.readchat.others.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import kotlinx.android.synthetic.main.dialog_forgot_password.view.*

class ForgotPasswordDialog: DialogFragment() {

    // Firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
                    activity!!.toast("${activity!!.applicationContext.getString(R.string.snack_bar_check_email)}: $email")
                        mAuth.sendPasswordResetEmail(email).addOnSuccessListener{}
                } else {
                    fragmentManager?.let { ForgotPasswordDialog().show(it,"") }
                    activity!!.toast(
                        activity!!.applicationContext.getString(R.string.snack_bar_fill_all_fields)
                    )
                }
            }
            .setNegativeButton(getString(R.string.button_cancel)){_,_ ->}
            .create()
    }

}




