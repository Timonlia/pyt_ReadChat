package com.albertomoya.readchat.activities.others

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookChapterFavAdapter
import com.albertomoya.readchat.persistance.Chapter
import com.albertomoya.readchat.persistance.ChatUser
import com.albertomoya.readchat.persistance.FavouriteBook
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.ChatProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.*
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.recyclerViewBookEditProfileDetail
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.toolbar
import kotlinx.android.synthetic.main.activity_book_post_detail.*
import kotlinx.android.synthetic.main.activity_select_chapter_to_read_book.*
import kotlinx.android.synthetic.main.fragment_favourite_books.*

class SelectChapterToReadBookActivity : ToolbarActivity() {
    private lateinit var idBookPost: String
    private val bookProvider = BookProvider()
    private val mAuth = AuthProvider()
    private val userProvider = UsersProvider()
    private lateinit var recyclerView: RecyclerView
    private var notHaveChapter = false
    private var imageViewIsCheck: Boolean = false
    private val chatProvider = ChatProvider()
    private lateinit var chapterAdapter: BookChapterFavAdapter
    private val layoutManager by lazy { LinearLayoutManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_chapter_to_read_book)
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        recyclerView = recyclerViewBookEditProfileDetail as RecyclerView
        recyclerView.layoutManager = layoutManager
        idBookPost = intent.getStringExtra("id").toString()
        getPostBook(idBookPost)
        setUpFavIntoSelectChapterToRead(idBookPost)
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
                    textViewCategoryBookDetailFav.text = it.getString(
                        NamesCollection.COLLECTION_BOOK_CATEGORY)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)){
                    if (it.getString(NamesCollection.COLLECTION_BOOK_PHOTO) != "")
                        Glide.with(this).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).centerInside().override(350,350).into(imageViewPhotoBookDetailFav)
                    else
                        Glide.with(this).load(R.drawable.new_photo_post).centerInside().override(350,350).into(imageViewPhotoBookDetailFav)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)){
                    if (it.getString(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK) == "")
                        textViewDescriptionBookDetailFav.text = applicationContext.getString(R.string.post_not_have_description)
                    else
                        textViewDescriptionBookDetailFav.text = (it.getString(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK))
                }
            }
        }
    }

    private fun setUpFavIntoSelectChapterToRead(idBook: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()) {
                if (mAuth.getUid().toString() == (it.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString())){
                    imageViewFavSelectChapter.visibility = View.INVISIBLE
                } else {
                    userProvider.getIfBookIsAddToFav(mAuth.getUid().toString(),idBook).addOnSuccessListener {
                        if (it.exists()){
                            imageViewIsCheck = true
                            imageViewFavSelectChapter.isChecked = true
                            imageViewFavSelectChapter.setBackgroundDrawable(
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.ic_ic_filled
                                )
                            )
                        } else {
                            imageViewIsCheck = false
                            imageViewFavSelectChapter.isChecked = false
                            imageViewFavSelectChapter.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_ic_empty))
                        }
                    }
                    imageViewFavSelectChapter.setOnClickListener {
                        if (!imageViewIsCheck){
                            imageViewFavSelectChapter.setBackgroundDrawable(
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.ic_ic_filled
                                )
                            )

                            bookProvider.getPostById(idBook).addOnSuccessListener { itPostFilled ->
                                if (itPostFilled.exists()){
                                    val favBook = FavouriteBook()
                                    favBook.uidFavChatBook = itPostFilled.getString(NamesCollection.COLLECTION_BOOK_UID_CHAT).toString()
                                    favBook.uidFavBook = itPostFilled.getString(NamesCollection.COLLECTION_BOOK_UID).toString()
                                    userProvider.addFavouriteBook(mAuth.getUid().toString(),favBook)

                                    val chatUser = ChatUser()
                                    if (itPostFilled.getBoolean(NamesCollection.COLLECTION_BOOK_CHAT) == true) {
                                        chatUser.uidChat =
                                            itPostFilled.getString(NamesCollection.COLLECTION_BOOK_UID_CHAT).toString()
                                        chatUser.uidUser = mAuth.getUid().toString()
                                        userProvider.addChatBookFavourite(chatUser)
                                        chatProvider.addUserToChat(chatUser.uidChat, chatUser)
                                    }
                                }
                            }
                            bookProvider.getPostById(idBookPost).addOnSuccessListener { itPost ->
                                if (itPost.exists()) {
                                    userProvider.getUser(itPost.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString()).addOnSuccessListener { itUser ->
                                        if (itUser.exists()){
                                            val user = User()
                                            user.UID = itUser.getString(NamesCollection.COLLECTION_USER_UID).toString()
                                            user.quantityFollowers = itUser[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString().toInt() +1
                                            Log.i("USER","${user.UID} votes ${user.quantityFollowers}")
                                            userProvider.updateVote(user)
                                        }
                                    }
                                }
                            }
                            imageViewIsCheck = true
                            // imageViewFavSelectChapter.isChecked = true
                        } else {
                            imageViewFavSelectChapter.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_ic_empty))
                            userProvider.deleteBookFav(mAuth.getUid().toString(),idBook)
                            bookProvider.getPostById(idBook).addOnSuccessListener { itPostEmpty ->
                                if (itPostEmpty.exists()){
                                    userProvider.deleteChatUser(mAuth.getUid().toString(),itPostEmpty.getString(NamesCollection.COLLECTION_BOOK_UID_CHAT).toString())
                                    chatProvider.deleteChatUser(mAuth.getUid().toString(),itPostEmpty.getString(NamesCollection.COLLECTION_BOOK_UID_CHAT).toString())
                                    userProvider.getUser(itPostEmpty.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString()).addOnSuccessListener { itUser ->
                                        if (itUser.exists()){
                                            val user = User()
                                            user.UID = itUser.getString(NamesCollection.COLLECTION_USER_UID).toString()
                                            user.quantityFollowers = itUser[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString().toInt() -1
                                            Log.i("USER","${user.UID} votes ${user.quantityFollowers}")
                                            userProvider.updateVote(user)
                                        }
                                    }
                                }
                            }
                            imageViewIsCheck = false
                            // imageViewFavSelectChapter.isChecked = false
                        }
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val chapter: Query = bookProvider.getAllCapsByBook(idBookPost)
        chapter.get().addOnCompleteListener {
            if (it.isSuccessful){
                if (!it.result!!.isEmpty){
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
                    notHaveChapter = true
                    textViewHaveChapterOtherUser.text = applicationContext.getString(R.string.chapter_have_chapter)
                } else {
                    notHaveChapter = false
                    textViewHaveChapterOtherUser.text = applicationContext.getString(R.string.chapter_not_have_chapter)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (notHaveChapter) chapterAdapter.stopListening()
    }
}