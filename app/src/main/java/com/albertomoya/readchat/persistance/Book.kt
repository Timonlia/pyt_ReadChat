package com.albertomoya.readchat.persistance

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Book constructor() {
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    var titleBook: String = ""
    var descriptionBook: String = ""
    var emailAuthor: String = ""
    var uidAuthor = ""
    var quantityCaps: Int = 0
    var categoryBook: String = ""
    var chatBook: Boolean = false
    var photoBook: String = ""
    var dateCreateBook = sdf.format(Date())
    var UID: String = ""
    var UIDChat: String = ""
    var historyText: String = ""
    init {
        this.UID
        this.titleBook
        this.descriptionBook
        this.emailAuthor
        this.uidAuthor
        this.quantityCaps = 0
        this.categoryBook
        this.chatBook
        this.photoBook
        this.dateCreateBook
        this.UIDChat
        this.historyText
    }
}