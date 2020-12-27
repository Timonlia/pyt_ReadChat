package com.albertomoya.readchat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.BookPostAdapter
import com.albertomoya.readchat.others.GridSpacingItemDecoration
import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.providers.AddBookProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val bookProvider = AddBookProvider()
    private lateinit var bookPostAdapter: BookPostAdapter
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val newLinerLayoutManager = GridLayoutManager(rootView.context,2)
        recyclerView = rootView.recyclerViewBookHome as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2,10, true))
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
        bookPostAdapter = BookPostAdapter(option, rootView.context)
        recyclerView.adapter = bookPostAdapter
        bookPostAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        bookPostAdapter.stopListening()
    }
}