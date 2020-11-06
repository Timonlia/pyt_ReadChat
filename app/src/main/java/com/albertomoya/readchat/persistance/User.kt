package com.albertomoya.readchat.persistance


import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class User constructor() {
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    var nick: String = ""
    var email: String = ""
    var UID: String = ""
    var fullName: String = ""
    var createdOn = sdf.format(Date())
    var quantityBooksUserCreated: Int = 0
    var quantityFollowers: Int = 0
    var descriptionUser: String = ""

    init {
        this.nick
        this.email
        this.UID
        this.fullName = ""
        this.createdOn
        this.quantityBooksUserCreated = 0
        this.quantityFollowers = 0
        this.descriptionUser = ""
    }

}