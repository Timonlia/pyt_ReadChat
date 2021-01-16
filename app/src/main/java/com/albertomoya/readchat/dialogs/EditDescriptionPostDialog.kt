package com.albertomoya.readchat.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.toast
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_edit_post_description.view.*

class EditDescriptionPostDialog(val uid: String): DialogFragment() {
    // Firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val bookProvider = BookProvider()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit_post_description, null)


        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_new_description))
            .setView(view)
            .setPositiveButton(getString(R.string.button_ok)){ _, _ ->
                val description = view.editTextEditDescriptionDialog.text.toString()
                if (description.isNotEmpty()){
                    val book = Book()
                    book.UID = uid
                    book.descriptionBook = description
                    bookProvider.updateBookDescription(book)
                } else {
                    fragmentManager?.let { EditDescriptionPostDialog(uid).show(it,"") }
                    activity!!.toast(
                        activity!!.applicationContext.getString(R.string.snack_bar_fill_all_fields)
                    )
                }
            }
            .setNegativeButton(getString(R.string.button_cancel)){ _, _ ->}
            .create()
    }
}