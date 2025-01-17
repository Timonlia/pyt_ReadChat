package com.albertomoya.readchat.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.BookProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), MaterialSearchBar.OnSearchActionListener {

    private lateinit var recyclerView: RecyclerView
    private val bookProvider = BookProvider()
    private val mAuth = AuthProvider()
    private var adapterTitle : Boolean = false
    private var adapterCategory : Boolean = false
    private lateinit var bookPostAdapterHome: BookPostAdapterHome
    private lateinit var bookPostAdapterHomeTitle: BookPostAdapterHome
    private lateinit var bookPostAdapterHomeCategory: BookPostAdapterHome
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        activity!!.title = activity!!.applicationContext.getString(R.string.app_name)
        val newLinerLayoutManager = GridLayoutManager(rootView.context, 3)
        recyclerView = rootView.recyclerViewBookHome as RecyclerView
        recyclerView.layoutManager = newLinerLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 10, true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        rootView.searchBar.setOnSearchActionListener(this)

        return rootView


    }
    private fun getAllPost(){
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
        bookPostAdapterHome.notifyDataSetChanged()
        recyclerView.adapter = bookPostAdapterHome
        bookPostAdapterHome.startListening()
    }

    private fun nullPost(){
        val books: Query = bookProvider.getPostByTitle("null")
        books.get().addOnCompleteListener {
            val option: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>().setQuery(
                books,
                Book::class.java
            ).build()
            bookPostAdapterHomeTitle =
                BookPostAdapterHome(
                    option,
                    rootView.context
                )
            bookPostAdapterHomeTitle.notifyDataSetChanged()
            recyclerView.adapter = bookPostAdapterHomeTitle
            bookPostAdapterHomeTitle.startListening()

            nullPosts.visibility = View.VISIBLE


        }
    }
    private fun searchByTitle(title: String){
        val books: Query = bookProvider.getPostByTitle(title)
        books.get().addOnCompleteListener {
            if (it.isSuccessful){
                if (!it.result!!.isEmpty){
                    nullPosts.visibility = View.INVISIBLE
                    val option: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>().setQuery(
                        books,
                        Book::class.java
                    ).build()
                    bookPostAdapterHomeTitle =
                        BookPostAdapterHome(
                            option,
                            rootView.context
                        )
                    bookPostAdapterHomeTitle.notifyDataSetChanged()
                    recyclerView.adapter = bookPostAdapterHomeTitle
                    bookPostAdapterHomeTitle.startListening()
                    adapterTitle = true
                } else {
                    adapterTitle = false
                }
            }
        }
    }

    private fun searchByCategory(category: String){
        val books: Query = bookProvider.getPostByCategory(category)
        books.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (!it.result!!.isEmpty) {
                    nullPosts.visibility = View.INVISIBLE
                    val option: FirestoreRecyclerOptions<Book> =
                        FirestoreRecyclerOptions.Builder<Book>().setQuery(
                            books,
                            Book::class.java
                        ).build()
                    bookPostAdapterHomeCategory =
                        BookPostAdapterHome(
                            option,
                            rootView.context
                        )
                    bookPostAdapterHomeCategory.notifyDataSetChanged()
                    recyclerView.adapter = bookPostAdapterHomeCategory
                    bookPostAdapterHomeCategory.startListening()
                    adapterCategory = true
                } else {
                    adapterCategory = false
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        nullPosts.visibility = View.INVISIBLE
        getAllPost()
    }

    override fun onStop() {
        super.onStop()

        if (adapterCategory)
            bookPostAdapterHomeCategory.stopListening()
        if (adapterTitle)
            bookPostAdapterHomeTitle.stopListening()

        bookPostAdapterHome.stopListening()
    }

    override fun onButtonClicked(buttonCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        nullPosts.visibility = View.INVISIBLE
        searchByTitle(text.toString())
        searchByCategory(text.toString())

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (!adapterCategory && !adapterTitle)
                nullPost()
        },300)


    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (!enabled){
            nullPosts.visibility = View.INVISIBLE
            getAllPost()
        }
    }
}