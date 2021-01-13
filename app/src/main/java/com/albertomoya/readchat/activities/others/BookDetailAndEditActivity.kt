package com.albertomoya.readchat.activities.others


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookChapterAdapter
import com.albertomoya.readchat.dialogs.AddChapter
import com.albertomoya.readchat.persistance.Chapter
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.*

class BookDetailAndEditActivity : ToolbarActivity() {
    private lateinit var idBook: String
    private val bookProvider = BookProvider()
    private lateinit var recyclerView: RecyclerView
    private lateinit var chapterAdapter: BookChapterAdapter
    private var notHaveChapters = false
    private val layoutManager by lazy { LinearLayoutManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail_and_edit)
        idBook = intent.getStringExtra("id").toString()
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        recyclerView = recyclerViewBookEditProfileDetail as RecyclerView
        recyclerView.layoutManager = layoutManager
        getPostBook(idBook)
        onClicksButtons()

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
    private fun onClicksButtons(){
        cardViewAddChapter.setOnClickListener {
            bookProvider.getPostById(idBook).addOnSuccessListener {
                if (it.exists()){
                    if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)) {
                        Log.i("hola", it[NamesCollection.COLLECTION_BOOK_QUANTITY_CAPS].toString())
                       AddChapter(
                           idBook, it.getString(NamesCollection.COLLECTION_BOOK_TITLE).toString(),
                           it[NamesCollection.COLLECTION_BOOK_QUANTITY_CAPS].toString()
                       ).show(supportFragmentManager, "")
                    }
                }
            }
        }
    }
    private fun getPostBook(idBookPost: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)){
                    editTexTitleBookDetailEditCurrentUser.setText(it.getString(NamesCollection.COLLECTION_BOOK_TITLE))
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CATEGORY)){
                    textViewCategoryBookDetailEditCurrentUser.text = it.getString(NamesCollection.COLLECTION_BOOK_CATEGORY)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)){
                    if (it.getString(NamesCollection.COLLECTION_BOOK_PHOTO) != "")
                        Glide.with(this).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).centerInside().override(
                            350,
                            350
                        ).into(imageViewPhotoBookDetailEditCurrentUser)
                    else
                        Glide.with(applicationContext).load(R.drawable.new_photo_post).centerInside().override(
                            350, 350)
                            .into(imageViewPhotoBookDetailEditCurrentUser)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)){
                    if (it.getString(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK) == "")
                        editTextDescriptionBookDetailEditCurrentUser.setText(
                            applicationContext.getString(
                                R.string.post_not_have_description
                            )
                        )
                    else
                        editTextDescriptionBookDetailEditCurrentUser.setText(
                            it.getString(
                                NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK
                            )
                        )

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val chapter: Query = bookProvider.getAllCapsByBook(idBook)
        chapter.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (!it.result!!.isEmpty) {
                    val option: FirestoreRecyclerOptions<Chapter> = FirestoreRecyclerOptions.Builder<Chapter>().setQuery(
                        chapter,
                        Chapter::class.java
                    ).build()
                    chapterAdapter =
                        BookChapterAdapter(
                            option,
                            this
                        )
                    recyclerView.adapter = chapterAdapter
                    chapterAdapter.startListening()
                    notHaveChapters = true
                    textViewHaveChapterCurrentUser.text = applicationContext.getString(R.string.chapter_have_chapter)
                } else {
                    notHaveChapters = false
                    textViewHaveChapterCurrentUser.text = applicationContext.getString(R.string.chapter_not_have_chapter)
                }
            }
        }


    }

    override fun onStop() {
        super.onStop()
        if (notHaveChapters) chapterAdapter.stopListening()

    }
}