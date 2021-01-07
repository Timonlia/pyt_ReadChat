package com.albertomoya.readchat.persistance

import java.util.*

class Message constructor(){
    var uidUser: String = ""
    var message: String = ""
    var userPhoto: String = ""
    var timestamp: Date = Date()
    var uidChat: String = ""
    init {
        this.message
        this.timestamp
        this.uidChat
        this.uidUser
        this.userPhoto
    }
}