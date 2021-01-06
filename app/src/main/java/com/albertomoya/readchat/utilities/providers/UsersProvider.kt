package com.albertomoya.readchat.utilities.providers

import com.albertomoya.readchat.persistance.FavouriteBook
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class UsersProvider {
    private val mDb: CollectionReference = FirebaseFirestore.getInstance().collection(NamesCollection.COLLECTION_USER)

    fun getUser(id: String): Task<DocumentSnapshot> {
        return mDb.document(id).get()
    }

    fun getUserForSnapshot(id: String): DocumentReference {
        return mDb.document(id)
    }

    fun createUser(newUser: User): Task<Void> {
       return mDb.document(newUser.UID).set(newUser)
    }

    fun updateUserProfile(user: User): Task<Void> {
        val updateProfileMap = HashMap<String, Any>()
        updateProfileMap[NamesCollection.COLLECTION_USER_NICK] = user.nick
        updateProfileMap[NamesCollection.COLLECTION_USER_FULL_NAME] = user.fullName
        updateProfileMap[NamesCollection.COLLECTION_USER_DESCRIPTION] = user.descriptionUser
        return mDb.document(user.UID).update(updateProfileMap)
    }

    fun updateUserPhoto(user: User): Task<Void> {
        val updateProfileMap = HashMap<String, Any>()
        updateProfileMap[NamesCollection.COLLECTION_USER_PHOTO_URL] = user.photoUrl
        return mDb.document(user.UID).update(updateProfileMap)
    }

    fun updateUserPhotoBackground(user: User): Task<Void> {
        val updateProfileMap = HashMap<String, Any>()
        updateProfileMap[NamesCollection.COLLECTION_USER_PHOTO_BACKGROUND] = user.photoBackground
        return mDb.document(user.UID).update(updateProfileMap)
    }
    fun updateUserBooks(user: User): Task<Void> {
        val updateBooks = HashMap<String, Any>()
        updateBooks[NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE] = user.quantityBooksUserCreated
        return mDb.document(user.UID).update(updateBooks)
    }

    fun addFavouriteBook(userId: String, favBook: FavouriteBook): Task<Void> {
        return mDb.document(userId).collection(NamesCollection.COLLECTION_USER_FAV_BOOK).document(favBook.uidFavBook).set(favBook)
    }

    fun getIfBookIsAddToFav(userId: String, idBook: String): Task<DocumentSnapshot> {
        return mDb.document(userId).collection(NamesCollection.COLLECTION_USER_FAV_BOOK).document(idBook).get()
    }

    fun deleteBookFav(userId: String, idBook: String): Task<Void> {
        return mDb.document(userId).collection(NamesCollection.COLLECTION_USER_FAV_BOOK).document(idBook).delete()
    }
    fun getAllFavourites(userId: String): Query{
        return mDb.document(userId).collection(NamesCollection.COLLECTION_USER_FAV_BOOK)
    }

}