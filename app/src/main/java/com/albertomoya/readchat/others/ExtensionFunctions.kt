package com.albertomoya.readchat.others

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.albertomoya.readchat.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

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
fun EditText.onChange(validation: (String) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {
            validation(editable.toString())
        }
    })
}
// Validaciones con patrones
fun Activity.isValidEmail(email: String): Boolean{
    val emailPatter = Patterns.EMAIL_ADDRESS
    return emailPatter.matcher(email).matches()
}
fun Activity.isValidPassword(password: String): Boolean{
    // Necesita Contener -> 1 Num / 1 Minúscula / 1 Mayúscula / 1 Special- > (?=.*[@#$%^&+=!]) / Min Caracteres 6
    val passwordPatter = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
    val pattern = Pattern.compile(passwordPatter)
    return pattern.matcher(password).matches()
}
fun Activity.isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}
