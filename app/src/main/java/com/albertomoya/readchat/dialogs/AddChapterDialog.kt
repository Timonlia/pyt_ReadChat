package com.albertomoya.readchat.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.toast
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.persistance.Chapter
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.BookProvider
import kotlinx.android.synthetic.main.dialog_add_chapter.view.*

class AddChapterDialog constructor(var idBook: String, var titleBook: String, var countChapter: String): DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_add_chapter, null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.title_new_chapter))
            .setView(view)
            .setPositiveButton(getString(R.string.button_ok)){_,_ ->
                val title = view.textInputAddChapterTitle.text.toString()
                if (title.isNotEmpty()){
                    val chapter = Chapter()
                    chapter.UID = titleBook+"_"+title
                    chapter.titleBook = titleBook
                    chapter.UIDBook = idBook
                    chapter.emailAuthor = AuthProvider().getEmail().toString()
                    chapter.textChapter = ""
                    chapter.uidAuthor = AuthProvider().getUid().toString()
                    chapter.titleChapter = title
                    chapter.countChapter = countChapter.toInt()+1

                    BookProvider().addCaps(idBook,chapter).addOnCompleteListener {
                        if (it.isSuccessful){
                            val book = Book()
                            book.quantityCaps = countChapter.toInt()+1
                            book.UID = idBook
                            BookProvider().updateBookQuantityCaps(book).addOnCompleteListener {

                            }
                        }
                    }



                } else {
                    fragmentManager?.let { AddChapterDialog(idBook,titleBook,countChapter).show(it,"") }
                    activity!!.toast(activity!!.applicationContext.getString(R.string.snack_bar_fill_all_fields))
                }
            }
            .setNegativeButton(getString(R.string.button_cancel)){_,_ ->}
            .create()
    }

}