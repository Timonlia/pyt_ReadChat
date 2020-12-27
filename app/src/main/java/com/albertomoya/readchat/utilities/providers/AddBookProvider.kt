package com.albertomoya.readchat.utilities.providers

import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class AddBookProvider {
    private val mDb: CollectionReference = FirebaseFirestore.getInstance().collection(NamesCollection.COLLECTION_BOOK)

    fun getBook(id: String): Task<DocumentSnapshot> {
        return mDb.document(id).get()
    }

    fun getBookForSnapshot(id: String): DocumentReference {
        return mDb.document(id)
    }

    fun createBook(newBook: Book): Task<Void> {
        return mDb.document(newBook.UID).set(newBook)
    }

    fun getAll(): Query{
        return mDb.orderBy(NamesCollection.COLLECTION_BOOK_TITLE,Query.Direction.DESCENDING)
    }
}