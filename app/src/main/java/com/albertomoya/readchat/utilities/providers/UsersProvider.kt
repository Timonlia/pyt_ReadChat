package com.albertomoya.readchat.utilities.providers

import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UsersProvider {
    private val mDb: CollectionReference = FirebaseFirestore.getInstance().collection(NamesCollection.COLLECTION_USER)

    fun getUser(id: String): Task<DocumentSnapshot> {
        return mDb.document(id).get()
    }

    fun createUser(newUser: User): Task<Void> {
       return mDb.document(newUser.UID).set(newUser)
    }

    fun updateUser(user: User): Task<Void> {
        val updateProfileMap = HashMap<String, Any>()
        updateProfileMap[NamesCollection.COLLECTION_USER_NICK] = user.nick
        updateProfileMap[NamesCollection.COLLECTION_USER_FULL_NAME] = user.fullName
        updateProfileMap[NamesCollection.COLLECTION_USER_DESCRIPTION] = user.descriptionUser
        return mDb.document(user.UID).update(updateProfileMap)
    }

}