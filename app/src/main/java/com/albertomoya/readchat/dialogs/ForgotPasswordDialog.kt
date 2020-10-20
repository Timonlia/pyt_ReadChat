package com.albertomoya.readchat.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R

class ForgotPasswordDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_forgot_password, null)



        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.title_dialog_forgot_password))
            .setView(view)
            .setPositiveButton(getString(R.string.button_ok)){_,_ ->

            }
            .setNegativeButton(getString(R.string.button_cancel)){_,_ ->

            }
            .create()
    }

}




