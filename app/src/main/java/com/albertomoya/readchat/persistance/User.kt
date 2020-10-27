package com.albertomoya.readchat.persistance


import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class User constructor(private var userNickName: String, private var emailUser: String, private var uidUser: String) {
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    var userNick: String = ""
    var email: String = ""
    var uid: String = ""
    var fullName: String = ""
    var dateUser = sdf.format(Date())
    var quantityBooksUserCreate: Int = 0

    init {
        this.userNick = userNickName
        this.email = emailUser
        this.uid = uidUser
        this.fullName = ""
        this.dateUser
        this.quantityBooksUserCreate = 0
    }

}