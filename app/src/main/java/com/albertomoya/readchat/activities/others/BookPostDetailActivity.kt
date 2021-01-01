package com.albertomoya.readchat.activities.others

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AddBookProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_book_post_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar

class BookPostDetailActivity : ToolbarActivity() {
    private lateinit var idBookPost: String
    private val bookProvider = AddBookProvider()
    private val mAuth = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_post_detail)
        idBookPost = intent.getStringExtra("id").toString()
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        getPostBook(idBookPost)
        onClicksButton()
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

    private fun getPostBook(idBookPost: String){
        bookProvider.getPostById(idBookPost).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_BOOK_TITLE)){
                    textViewTitleBookDetail.text = it.getString(NamesCollection.COLLECTION_BOOK_TITLE)
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_CATEGORY)){
                    textViewCategoryBookDetail.text = "Categoria: ${it.getString(NamesCollection.COLLECTION_BOOK_CATEGORY)}"
                }
                if (it.contains(NamesCollection.COLLECTION_BOOK_PHOTO)){
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_PHOTO).isNullOrEmpty()){
                        Glide.with(this).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).centerInside().override(350,350).into(imageViewPhotoBookDetail)
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
        mAuth.getUser(uid).addOnSuccessListener {
            if (it.exists()){
                if (it.contains(NamesCollection.COLLECTION_USER_NICK)){
                    userNameAuthorDetail.text = it.getString(NamesCollection.COLLECTION_USER_NICK)
                }
                if (it.contains(NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS)){
                    followsAuthorDetail.text = "Seguidores: ${it[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString()}"
                }
                if (it.contains(NamesCollection.COLLECTION_USER_PHOTO_URL)){
                    Glide.with(this).load(it.getString(NamesCollection.COLLECTION_USER_PHOTO_URL)).override(350,350).into(photoUserDetail)
                }
            }
        }
    }


}