package com.albertomoya.readchat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookPostAdapterHome
import com.albertomoya.readchat.others.GridSpacingItemDecoration
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val bookProvider = BookProvider()
    private val mAuth = AuthProvider()
    private lateinit var bookPostAdapterHome: BookPostAdapterHome
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val newLinerLayoutManager = GridLayoutManager(rootView.context,3)
        recyclerView = rootView.recyclerViewBookHome as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3,10, true))
        recyclerView.itemAnimator = DefaultItemAnimator()

        return rootView


    }

    override fun onStart() {
        super.onStart()
        val books: Query = bookProvider.getAll()
        val option: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>().setQuery(
            books,
            Book::class.java
        ).build()
        bookPostAdapterHome =
            BookPostAdapterHome(
                option,
                rootView.context
            )
        recyclerView.adapter = bookPostAdapterHome
        bookPostAdapterHome.startListening()
    }

    override fun onStop() {
        super.onStop()
        bookPostAdapterHome.stopListening()
    }
}