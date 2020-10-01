package com.albertomoya.readchat.others

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import com.albertomoya.readchat.R
import com.google.android.material.snackbar.Snackbar

fun ImageView.animation(animacion: Animation?) { animation(animacion) }
inline fun <reified T : Activity>Activity.goToActivity(noinline init: Intent.() -> Unit = {}){
    val intent = Intent(this,T::class.java)
    startActivity(intent)
}
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {Toast.makeText(this,message,duration).show()}
fun Activity.snackBar(message: CharSequence, view: View? = findViewById(R.id.container),
                      duration: Int = Snackbar.LENGTH_LONG, action: String? = null,
                      actionEvt: (v: View) -> Unit = {}){
    if (view != null){
        val snackbar = Snackbar.make(view!!,message,duration)
        if (!action.isNullOrEmpty()){
            snackbar.setAction(action,actionEvt)
        }
        snackbar.show()
    }
}