package com.albertomoya.readchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookFavAdapter
import com.albertomoya.readchat.adapters.BookPostAdapterHome
import com.albertomoya.readchat.others.GridSpacingItemDecoration
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.persistance.FavouriteBook
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_favourite_books.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class FavouriteBooksFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val usersProvider = UsersProvider()
    private val mAuth = AuthProvider()
    private lateinit var bookPostAdapterFav: BookFavAdapter
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
         rootView = inflater.inflate(R.layout.fragment_favourite_books, container, false)
        val newLinerLayoutManager = GridLayoutManager(rootView.context,3)
        activity!!.title = activity!!.applicationContext.getString(R.string.title_fav_posts)
        recyclerView = rootView.recyclerViewBookFav as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3,10, true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        return rootView
    }
    override fun onStart() {
        super.onStart()
        val books: Query = usersProvider.getAllFavourites(mAuth.getUid().toString())
        val option: FirestoreRecyclerOptions<FavouriteBook> = FirestoreRecyclerOptions.Builder<FavouriteBook>().setQuery(
            books,
            FavouriteBook::class.java
        ).build()
        bookPostAdapterFav =
            BookFavAdapter(
                option,
                rootView.context
            )
        recyclerView.adapter = bookPostAdapterFav
        bookPostAdapterFav.startListening()
    }

    override fun onStop() {
        super.onStop()
        bookPostAdapterFav.stopListening()
    }
}