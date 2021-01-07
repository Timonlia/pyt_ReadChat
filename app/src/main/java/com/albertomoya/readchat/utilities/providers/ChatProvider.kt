package com.albertomoya.readchat.utilities.providers

import com.albertomoya.readchat.persistance.Chat
import com.albertomoya.readchat.persistance.ChatUser
import com.albertomoya.readchat.persistance.Message
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class ChatProvider {
    private val mDb: CollectionReference = FirebaseFirestore.getInstance().collection(
        NamesCollection.COLLECTION_CHAT)

    fun createChat(chat: Chat): Task<Void> {
        return mDb.document(chat.uidChat).set(chat)
    }

    fun addUserToChat(uidChat: String, userChat: ChatUser): Task<Void> {
        return mDb.document(uidChat).collection(NamesCollection.COLLECTION_CHAT_USERS).document(userChat.uidUser).set(userChat)
    }

    fun addMessageToChat(uidChat: String,message: Message): Task<Void> {
        return mDb.document(uidChat).collection(NamesCollection.COLLECTION_CHAT_MESSAGES).document().set(message)
    }

    fun deleteChatUser(userId: String, idChat: String): Task<Void> {
        return mDb.document(idChat).collection(NamesCollection.COLLECTION_CHAT_USERS).document(userId).delete()
    }

    fun getChatById(idChat: String): Task<DocumentSnapshot>{
        return mDb.document(idChat).get()
    }

    fun getAllMessages(uidChat: String): Query {
        return mDb.document(uidChat).collection(NamesCollection.COLLECTION_CHAT_MESSAGES).orderBy(NamesCollection.COLLECTION_CHAT_MESSAGES_TIMESTAMP, Query.Direction.DESCENDING)
    }
}