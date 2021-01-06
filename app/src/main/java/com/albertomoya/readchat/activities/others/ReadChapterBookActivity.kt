package com.albertomoya.readchat.activities.others


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.BookProvider
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.toolbar
import kotlinx.android.synthetic.main.activity_read_chapter_book.*

class ReadChapterBookActivity : ToolbarActivity() {
    private lateinit var idChapter: String
    private lateinit var idBook: String
    private val bookProvider = BookProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_chapter_book)
        idChapter = intent.getStringExtra("id").toString()
        Log.i("id",idChapter)
        idBook = intent.getStringExtra("idBook").toString()
        Log.i("ids",idBook)
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        getChapter(idChapter,idBook)
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

    private fun getChapter(idChapter: String, idBook: String){
        bookProvider.getChapterById(idChapter,idBook).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_BOOK_CAPS_TEXT)){
                    textViewHistoryText.text = it.getString(NamesCollection.COLLECTION_BOOK_CAPS_TEXT)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CAPS_TITLE_CAP)){
                    _toolbar!!.title = it.getString(NamesCollection.COLLECTION_BOOK_CAPS_TITLE_CAP)
                }
            }
        }
    }
}