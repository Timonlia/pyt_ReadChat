package com.albertomoya.readchat.utilities.providers

import com.albertomoya.readchat.persistance.Book
import com.albertomoya.readchat.persistance.Chapter
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class BookProvider {
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
        return mDb.orderBy(NamesCollection.COLLECTION_BOOK_TITLE,Query.Direction.ASCENDING)
    }

    fun getAllBooksByUser(id: String): Query{
        return mDb.whereEqualTo(NamesCollection.COLLECTION_BOOK_UID_AUTHOR, id).orderBy(NamesCollection.COLLECTION_BOOK_DATE_CREATE_ON)
    }

    fun getPostById(idBook: String): Task<DocumentSnapshot>{
        return mDb.document(idBook).get()
    }

    fun getPostByCategory(category: String): Query{
        return mDb.orderBy(NamesCollection.COLLECTION_BOOK_CATEGORY).startAt(category).endAt(category+'\uf8ff')
    }

    fun getPostByTitle(title: String): Query{
        return mDb.orderBy(NamesCollection.COLLECTION_BOOK_TITLE).startAt(title).endAt(title+'\uf8ff')
    }

    fun updateBook(book: Book): Task<Void> {
        val updateProfileMap = HashMap<String, Any>()
        updateProfileMap[NamesCollection.COLLECTION_BOOK_QUANTITY_CAPS] = book.quantityCaps
        return mDb.document(book.UID).update(updateProfileMap)
    }

    fun addCaps(idBook: String, cap: Chapter): Task<Void>{
        return mDb.document(idBook).collection(NamesCollection.COLLECTION_BOOK_CAPS).document(cap.UID).set(cap)
    }

    fun getAllCapsByBook(idBook: String): Query{
        return mDb.document(idBook).collection(NamesCollection.COLLECTION_BOOK_CAPS)
            .orderBy(NamesCollection.COLLECTION_BOOK_CAPS_COUNT_CHAPTER)
    }

    fun getChapterById(idChapter: String, idBook: String): Task<DocumentSnapshot>{
        return mDb.document(idBook).collection(NamesCollection.COLLECTION_BOOK_CAPS).document(idChapter).get()
    }

    fun updateTextBook(chapter: Chapter): Task<Void> {
        val updateChapterMap = HashMap<String, Any>()
        updateChapterMap[NamesCollection.COLLECTION_BOOK_CAPS_TEXT] = chapter.textChapter
        return mDb.document(chapter.UIDBook).collection(NamesCollection.COLLECTION_BOOK_CAPS).document(chapter.UID).update(updateChapterMap)
    }

    fun deleteChapter(idChapter: String, idBook: String): Task<Void> {
        return mDb.document(idBook).collection(NamesCollection.COLLECTION_BOOK_CAPS).document(idChapter).delete()
    }



}