package com.albertomoya.readchat.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.login.SignInActivity
import com.albertomoya.readchat.others.goToActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutDialog: DialogFragment() {
    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return AlertDialog.Builder(context!!)
            .setTitle(R.string.logout_want_it)
            .setPositiveButton(R.string.button_ok){_,_ ->
                mAuth.signOut()
                activity!!.goToActivity<SignInActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity!!.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                }
                activity!!.finish()
            }
            .setNegativeButton(R.string.button_cancel){_,_ ->

            }
            .create()
    }
}