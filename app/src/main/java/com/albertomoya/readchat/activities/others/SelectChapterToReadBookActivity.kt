package com.albertomoya.readchat.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookChapterAdapter
import com.albertomoya.readchat.adapters.BookChapterFavAdapter
import com.albertomoya.readchat.persistance.Chapter
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.*
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.recyclerViewBookEditProfileDetail
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.toolbar
import kotlinx.android.synthetic.main.activity_select_chapter_to_read_book.*
import kotlinx.android.synthetic.main.fragment_favourite_books.*

class SelectChapterToReadBookActivity : ToolbarActivity() {
    private lateinit var idBook: String
    private val bookProvider = BookProvider()
    private lateinit var recyclerView: RecyclerView
    private lateinit var chapterAdapter: BookChapterFavAdapter
    private val layoutManager by lazy { LinearLayoutManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_chapter_to_read_book)
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        recyclerView = recyclerViewBookEditProfileDetail as RecyclerView
        recyclerView.layoutManager = layoutManager
        idBook = intent.getStringExtra("id").toString()
        getPostBook(idBook)
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

    private fun getPostBook(idBookPost: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)){
                    textViewTitleBookDetailFav.text = (it.getString(NamesCollection.COLLECTION_BOOK_TITLE))
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CATEGORY)){
                    textViewCategoryBookDetailFav.text = "Categoria: ${it.getString(
                        NamesCollection.COLLECTION_BOOK_CATEGORY)}"
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)){
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_PHOTO).isNullOrEmpty()){
                        Glide.with(this).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).centerInside().override(350,350).into(imageViewPhotoBookDetailFav)
                    }
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)){
                    textViewDescriptionBookDetailFav.text = (it.getString(
                        NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val chapter: Query = bookProvider.getAllCapsByBook(idBook)

        val option: FirestoreRecyclerOptions<Chapter> = FirestoreRecyclerOptions.Builder<Chapter>().setQuery(
            chapter,
            Chapter::class.java
        ).build()
        chapterAdapter =
            BookChapterFavAdapter(
                option,
                this
            )
        recyclerView.adapter = chapterAdapter
        chapterAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        chapterAdapter.stopListening()
    }
}