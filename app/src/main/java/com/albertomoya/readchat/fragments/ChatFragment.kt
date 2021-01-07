package com.albertomoya.readchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.ChatAdapter
import com.albertomoya.readchat.persistance.ChatUser
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.ChatProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    private lateinit var rootView: View
    private val chatProvider = ChatProvider()
    private val userProvider = UsersProvider()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private val mAuth = AuthProvider()
    private val layoutManager by lazy { LinearLayoutManager(rootView.context) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = rootView.recyclerViewChats as RecyclerView
        recyclerView.layoutManager = layoutManager
        return rootView
    }

    private fun getAllChat(){
        val books: Query = userProvider.getAllChats(mAuth.getUid().toString())
        val option: FirestoreRecyclerOptions<ChatUser> = FirestoreRecyclerOptions.Builder<ChatUser>().setQuery(
            books,
            ChatUser::class.java
        ).build()
        chatAdapter =
            ChatAdapter(
                option,
                rootView.context
            )
        chatAdapter.notifyDataSetChanged()
        recyclerView.adapter = chatAdapter
        chatAdapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        getAllChat()
    }

    override fun onStop() {
        super.onStop()
        chatAdapter.stopListening()
    }
}