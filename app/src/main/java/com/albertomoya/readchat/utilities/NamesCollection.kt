package com.albertomoya.readchat.utilities

class NamesCollection {
    companion object{
        // USERS
        const val COLLECTION_USER: String = "Users"
        const val COLLECTION_USER_NICK: String = "nick"
        const val COLLECTION_USER_UID: String = "uid"
        const val COLLECTION_USER_EMAIL: String = "email"
        const val COLLECTION_USER_FULL_NAME: String = "fullName"
        const val COLLECTION_USER_DATE_CREATED_ON: String = "createdOn"
        const val COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE = "quantityBooksUserCreated"
        const val COLLECTION_USER_QUANTITY_FOLLOWERS = "quantityFollowers"
        const val COLLECTION_USER_DESCRIPTION = "descriptionUser"
        const val COLLECTION_USER_PHOTO_URL = "photoUrl"
        const val COLLECTION_USER_PHOTO_BACKGROUND = "photoBackground"
        // BOOK
        const val COLLECTION_BOOK = "Books"
        const val COLLECTION_BOOK_TITLE = "titleBook"
        const val COLLECTION_BOOK_CATEGORY = "categoryBook"
        const val COLLECTION_BOOK_DESCRIPTION_BOOK = "descriptionBook"
        const val COLLECTION_BOOK_UID = "uid"
        const val COLLECTION_BOOK_EMAIL_AUTHOR = "emailAuthor"
        const val COLLECTION_BOOK_UID_AUTHOR = "uidAuthor"
        const val COLLECTION_BOOK_QUANTITY_CAPS = "quantityCaps"
        const val COLLECTION_BOOK_CHAT = "chatBook"
        const val COLLECTION_BOOK_PHOTO = "photoBook"
        const val COLLECTION_BOOK_DATE_CREATE_ON = "dateCreateBook"
        const val COLLECTION_BOOK_UID_CHAT = "uidchat"
        const val COLLECTION_BOOK_HISTORY_TEXT = "historyText"
        // BOOK CAPS
        const val COLLECTION_BOOK_CAPS = "Capitulos"
        const val COLLECTION_BOOK_CAPS_TITLE_CAP = "titleChapter"
        const val COLLECTION_BOOK_CAPS_TEXT = "textChapter"
        const val COLLECTION_BOOK_CAPS_EMAIL = "emailAuthor"
        const val COLLECTION_BOOK_CAPS_UID_AUTHOR = "uidAuthor"
        const val COLLECTION_BOOK_CAPS_UID = "UID"
        const val COLLECTION_BOOK_CAPS_UID_BOOK = "uidbook"
        const val COLLECTION_BOOK_CAPS_TITLE_BOOK = "titleBook"
        const val COLLECTION_BOOK_CAPS_COUNT_CHAPTER = "countChapter"
        // FAV BOOK
        const val COLLECTION_USER_FAV_BOOK = "FavBook"
        const val COLLECTION_USER_FAV_BOOK_UID = "uidFavBook"
        const val COLLECTION_USER_FAV_BOOK_UID_CHAT = "uidFavChatBook"
        const val COLLECTION_USER_FAV_BOOK_UID_AUTHOR = "uidAuthor"
        // CHAT
        const val COLLECTION_CHAT = "Chats"
        const val COLLECTION_CHAT_UID = "uidChat"
        const val COLLECTION_CHAT_TIMESTAMP = "timestamp"
        const val COLLECTION_CHAT_NAME = "nameChat"
        const val COLLECTION_CHAT_PHOTO = "photoChat"

        // CHAT USER
        const val COLLECTION_CHAT_USERS = "UsersChat"
        const val COLLECTION_CHAT_USERS_UID_USER = "uidUser"
        const val COLLECTION_CHAT_USERS_UID_CHAT = "uidChat"

        // CHAT MESSAGE
        const val COLLECTION_CHAT_MESSAGES = "Messages"
        const val COLLECTION_CHAT_MESSAGES_UID_USER = "uidUser"
        const val COLLECTION_CHAT_MESSAGES_MESSAGE = "message"
        const val COLLECTION_CHAT_MESSAGES_PHOTO_USER = "userPhoto"
        const val COLLECTION_CHAT_MESSAGES_TIMESTAMP = "timestamp"
        const val COLLECTION_CHAT_MESSAGES_UID_CHAT = "uidChat"
    }
}