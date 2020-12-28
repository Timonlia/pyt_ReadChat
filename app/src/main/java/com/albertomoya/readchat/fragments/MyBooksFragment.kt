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
import com.albertomoya.readchat.adapters.BookPostAdapterCurrentUser
import com.albertomoya.readchat.adapters.BookPostAdapterHome
import com.albertomoya.readchat.others.GridSpacingItemDecoration
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.providers.AddBookProvider
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_my_books.view.*


class MyBooksFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val bookProvider = AddBookProvider()
    private val mAuth = AuthProvider()
    private lateinit var bookPostAdapterMyBooksFragment: BookPostAdapterCurrentUser
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_my_books, container, false)

        val newLinerLayoutManager = GridLayoutManager(rootView.context,3)
        recyclerView = rootView.recyclerViewBookCurrentUser as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3,10, true))

        recyclerView.itemAnimator = DefaultItemAnimator()
        return rootView
    }

    override fun onStart() {
        super.onStart()
        val books: Query = bookProvider.getAllBooksByCurrentUser(mAuth.getUid().toString())
        val option: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>().setQuery(
            books,
            Book::class.java
        ).build()
        bookPostAdapterMyBooksFragment =
            BookPostAdapterCurrentUser(
                option,
                rootView.context
            )
        recyclerView.adapter = bookPostAdapterMyBooksFragment
        bookPostAdapterMyBooksFragment.startListening()
    }

    override fun onStop() {
        super.onStop()
        bookPostAdapterMyBooksFragment.stopListening()
    }
}