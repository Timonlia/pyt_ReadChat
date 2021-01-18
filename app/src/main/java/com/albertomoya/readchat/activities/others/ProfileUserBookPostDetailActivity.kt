package com.albertomoya.readchat.activities.others

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookPostAdapterProfileDetail
import com.albertomoya.readchat.others.GridSpacingItemDecoration
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_profile_user_book_post_detail.*

class ProfileUserBookPostDetailActivity : ToolbarActivity() {

    private lateinit var idUserBookPost: String
    private val userProvider = UsersProvider()
    private val bookProvider = BookProvider()
    private var notHavePosts = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookPostAdapterProfileDetail: BookPostAdapterProfileDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user_book_post_detail)
        idUserBookPost = intent.getStringExtra("id").toString()
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        getInfoCurrentUserAndSetUpDataUser(idUserBookPost)
        val newLinerLayoutManager = GridLayoutManager(this,3)
        recyclerView = recyclerViewBookProfileDetail as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3,10, true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        listenerChangesCollection()
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

    private fun getInfoCurrentUserAndSetUpDataUser(uid: String){
        userProvider.getUser(uid).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_USER_NICK)){
                    textViewNickUserDetail.text = it.getString(NamesCollection.COLLECTION_USER_NICK)
                }
                if (it.contains(NamesCollection.COLLECTION_USER_DESCRIPTION)){
                    if (it.getString(NamesCollection.COLLECTION_USER_DESCRIPTION) == "") textViewDescriptionUserDetail.text = applicationContext.getString(R.string.user_not_have_description)
                    else textViewDescriptionUserDetail.text = it.getString(NamesCollection.COLLECTION_USER_DESCRIPTION)
                }
                if (it.contains(NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE)){
                    textViewBooksDetail.text = "${applicationContext.getString(R.string.books)}: ${it[NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE].toString()}"
                }
                if (it.contains(NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS)){
                    textViewFollowersDetail.text = "${applicationContext.getString(R.string.votes)}: ${it[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString()}"
                }
                if (it.contains(NamesCollection.COLLECTION_USER_PHOTO_URL)){
                    Glide.with(applicationContext).load(it.getString(NamesCollection.COLLECTION_USER_PHOTO_URL)).override(350,350).into(imageProfileFragmentDetail)
                }
                if (it.contains(NamesCollection.COLLECTION_USER_PHOTO_BACKGROUND)){
                    Glide.with(applicationContext).load(it.getString(NamesCollection.COLLECTION_USER_PHOTO_BACKGROUND)).override(350,350).into(imageProfileBackgroundFragmentDetail)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val books: Query = bookProvider.getAllBooksByUser(idUserBookPost)
        books.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (!it.result!!.isEmpty) {
                    val option: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>().setQuery(
                        books,
                        Book::class.java
                    ).build()
                    bookPostAdapterProfileDetail =
                        BookPostAdapterProfileDetail(
                            option,
                            this
                        )
                    recyclerView.adapter = bookPostAdapterProfileDetail
                    bookPostAdapterProfileDetail.startListening()
                    notHavePosts = true
                    textViewNotHavePosts.text = applicationContext.getString(R.string.post_have_posts)
                } else{
                    notHavePosts = false
                    textViewNotHavePosts.text = applicationContext.getString(R.string.post_not_have_post)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        if (notHavePosts) bookPostAdapterProfileDetail.stopListening()
    }

    private fun listenerChangesCollection(){
        userProvider.getUserForSnapshot(idUserBookPost).addSnapshotListener(object : java.util.EventListener,
            EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                getInfoCurrentUserAndSetUpDataUser(idUserBookPost)
            }
        })
    }
}