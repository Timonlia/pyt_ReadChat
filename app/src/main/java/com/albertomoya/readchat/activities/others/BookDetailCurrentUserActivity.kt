package com.albertomoya.readchat.activities.others

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_book_detail_current_user.*
import kotlinx.android.synthetic.main.activity_main.toolbar

class BookDetailCurrentUserActivity : ToolbarActivity() {
    private lateinit var idBookPost: String
    private val bookProvider = BookProvider()
    private val mAuth = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail_current_user)
        idBookPost = intent.getStringExtra("id").toString()
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        getPostBook(idBookPost)
        onClicksButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClicksButton(){
        floatingButtonSave.setOnClickListener {
            val book = Book()
            book.historyText = editTextBookDetailCurrentUser.text.toString()
            book.UID = idBookPost
            bookProvider.updateBookQuantityCaps(book).addOnCompleteListener {
                if (it.isSuccessful)
                    snackBar(applicationContext.getString(R.string.snack_bar_post_great_create))
                else
                    snackBar(applicationContext.getString(R.string.snack_bar_post_error_create))
            }
        }
    }

    private fun getPostBook(idBookPost: String) {
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()) {
                if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)) {
                    textViewTitleBookDetailCurrentUser.setText(it.getString(NamesCollection.COLLECTION_BOOK_TITLE))
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CATEGORY)) {
                    textViewCategoryBookDetailCurrentUser.text =
                        "${applicationContext.getString(R.string.category)}: ${it.getString(NamesCollection.COLLECTION_BOOK_CATEGORY)}"
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)) {
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_PHOTO).isNullOrEmpty()) {
                        Glide.with(this).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO))
                            .centerInside().override(350, 350)
                            .into(imageViewPhotoBookDetailCurrentUser)
                    }
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)) {
                    textViewDescriptionBookDetailCurrentUser.text =
                        it.getString(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_HISTORY_TEXT)) {
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_HISTORY_TEXT).isNullOrEmpty()){
                        editTextBookDetailCurrentUser.setText(it.getString(NamesCollection.COLLECTION_BOOK_HISTORY_TEXT))
                    }
                }
            }
        }
    }
}