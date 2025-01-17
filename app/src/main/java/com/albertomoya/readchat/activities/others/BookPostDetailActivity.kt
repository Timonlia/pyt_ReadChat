package com.albertomoya.readchat.activities.others

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.persistance.ChatUser
import com.albertomoya.readchat.persistance.FavouriteBook
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.ChatProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_book_post_detail.*
import kotlinx.android.synthetic.main.activity_main.toolbar

class BookPostDetailActivity : ToolbarActivity() {
    private lateinit var idBookPost: String
    private val bookProvider = BookProvider()
    private val userProvider = UsersProvider()
    private val mAuth = AuthProvider()
    private var imageViewIsCheck: Boolean = false
    private val chatProvider = ChatProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_post_detail)
        idBookPost = intent.getStringExtra("id").toString()
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        title = applicationContext.getString(R.string.title_detail_post)
        getPostBook(idBookPost)
        onClicksButton()
        setUpImageFavourites(idBookPost)
        listenerChangesCollection()
    }
    private fun onClicksButton(){
        buttonShowProfileDetail.setOnClickListener {
            bookProvider.getPostById(idBookPost).addOnSuccessListener {
                if (it.exists()){
                    if (it.contains(NamesCollection.COLLECTION_BOOK_UID_AUTHOR)){
                        val intent = Intent(this, ProfileUserBookPostDetailActivity::class.java)
                        intent.putExtra("id",it.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString())
                        startActivity(intent)
                        overridePendingTransition(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                        )
                    }
                }
            }
        }

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

    private fun setUpImageFavourites(idBook: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()) {
                if (mAuth.getUid().toString() == (it.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString())){
                    imageViewFavDetail.visibility = View.INVISIBLE
                } else {
                    userProvider.getIfBookIsAddToFav(mAuth.getUid().toString(),idBook).addOnSuccessListener {
                        if (it.exists()){
                            imageViewIsCheck = true
                            imageViewFavDetail.isChecked = true
                            imageViewFavDetail.setBackgroundDrawable(
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.ic_ic_filled
                                )
                            )
                        } else {
                            imageViewIsCheck = false
                            imageViewFavDetail.isChecked = false
                            imageViewFavDetail.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_ic_empty))
                        }
                    }
                    imageViewFavDetail.setOnClickListener {
                        if (!imageViewIsCheck){
                            imageViewFavDetail.setBackgroundDrawable(
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
                                    favBook.uidAuthor = itPostFilled.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString()
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
                            // imageViewFavDetail.isChecked = true
                        } else {
                            imageViewFavDetail.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_ic_empty))
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
                            // imageViewFavDetail.isChecked = false
                        }
                    }
                }
            }
        }


    }

    private fun getPostBook(idBookPost: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)){
                    textViewTitleBookDetail.text = it.getString(NamesCollection.COLLECTION_BOOK_TITLE)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CATEGORY)){
                    textViewCategoryBookDetail.text = it.getString(NamesCollection.COLLECTION_BOOK_CATEGORY)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)){
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_PHOTO).isNullOrEmpty()){
                        Glide.with(applicationContext).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).centerInside().override(350,350).into(imageViewPhotoBookDetail)
                    } else {
                        Glide.with(applicationContext).load(R.drawable.new_photo_post).centerInside().override(350,350).into(imageViewPhotoBookDetail)
                    }
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)){
                    textViewDescriptionBookDetail.text = it.getString(NamesCollection.COLLECTION_BOOK_DESCRIPTION_BOOK)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_UID_AUTHOR)){
                    getInfoAuthor(it.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString())
                }
            }
        }
    }

    private fun getInfoAuthor(uid: String){
        userProvider.getUser(uid).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_USER_NICK)){
                    userNameAuthorDetail.text = it.getString(NamesCollection.COLLECTION_USER_NICK)
                }
                if (it.contains(NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS)){
                    followsAuthorDetail.text = "${applicationContext.getString(R.string.votes)}: ${it[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString()}"
                }
                if (it.contains(NamesCollection.COLLECTION_USER_PHOTO_URL)){
                    Glide.with(applicationContext).load(it.getString(NamesCollection.COLLECTION_USER_PHOTO_URL)).override(350,350).into(photoUserDetail)
                }
            }
        }

    }

    private fun listenerChangesCollection(){
        bookProvider.getPostById(idBookPost).addOnSuccessListener { itPost ->
            if (itPost.exists()) {
                userProvider.getUserForSnapshot(itPost.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString()).addSnapshotListener(object : java.util.EventListener,
                    EventListener<DocumentSnapshot> {
                    override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                        getInfoAuthor(itPost.getString(NamesCollection.COLLECTION_BOOK_UID_AUTHOR).toString())
                    }
                })
            }
        }
    }
}