package com.albertomoya.readchat.others

import android.app.Activity
import android.content.Intent
import android.view.animation.Animation
import android.widget.ImageView

fun ImageView.animation(animacion: Animation?) { animation(animacion) }
inline fun <reified T : Activity>Activity.goToActivity(noinline init: Intent.() -> Unit = {}){
    val intent = Intent(this,T::class.java)
    startActivity(intent)
}