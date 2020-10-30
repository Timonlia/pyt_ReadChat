package com.albertomoya.readchat.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.google.firebase.auth.FirebaseAuth

class LogoutDialog: DialogFragment() {
    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return AlertDialog.Builder(context!!)
            .setTitle(R.string.logout_want_it)
            .setPositiveButton(R.string.button_ok){_,_ ->
                mAuth.signOut()
                activity!!.finish()
            }
            .setNegativeButton(R.string.button_cancel){_,_ ->

            }
            .create()
    }
}