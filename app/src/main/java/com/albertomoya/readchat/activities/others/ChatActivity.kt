package com.albertomoya.readchat.activities.others

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.adapters.MessageAdapter
import com.albertomoya.readchat.others.toast
import com.albertomoya.readchat.persistance.Message
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.ChatProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_book_detail_and_edit.toolbar
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : ToolbarActivity() {
    private val mAuth = AuthProvider()
    private val userProvider = UsersProvider()
    private val chatProvider = ChatProvider()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var idChat: String
    private var chatSubscription: ListenerRegistration? = null
    private val messageList: ArrayList<Message> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toolbarToLoad(toolbar as Toolbar)
        _toolbar?.setNavigationIcon(R.drawable.ic_back)
        idChat = intent.getStringExtra("id").toString()
        onClickButtons()
        setUpRecyclerView()
        subscribeToChatMessage()
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

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messageList, mAuth.getUid().toString())

        recyclerViewMessages.setHasFixedSize(true)
        recyclerViewMessages.layoutManager = layoutManager
        recyclerViewMessages.itemAnimator = DefaultItemAnimator()
        recyclerViewMessages.adapter = messageAdapter
    }

    private fun onClickButtons(){
        buttonSendMessage.setOnClickListener{
            val messageText = editTextWriteNewMessage.text.toString()
            if (messageText.isNotEmpty()){
                saveMessage(messageText)
                editTextWriteNewMessage.text.clear()
            }

        }
    }

    private fun saveMessage(messageText: String){
        userProvider.getUser(mAuth.getUid().toString()).addOnSuccessListener {
            if (it.exists()){
                val message = Message()
                message.uidUser = mAuth.getUid().toString()
                message.userPhoto = it.getString(NamesCollection.COLLECTION_USER_PHOTO_URL).toString()
                message.uidChat = idChat
                message.message = messageText
                chatProvider.addMessageToChat(idChat,message)
            }
        }
    }

    private fun subscribeToChatMessage(){
        chatSubscription = chatProvider.getAllMessages(idChat).addSnapshotListener(object : java.util.EventListener, EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {

                        return
                    }
                    snapshot?.let {
                        messageList.clear()
                        val messages = it.toObjects(Message::class.java)
                        messageList.addAll(messages.asReversed())
                        messageAdapter.notifyDataSetChanged()
                        recyclerViewMessages.smoothScrollToPosition(messageList.size)
                    }
                }
            })
    }
}