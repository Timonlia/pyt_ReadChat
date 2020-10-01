package com.albertomoya.readchat.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albertomoya.readchat.MainActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.dialogs.ForgotPasswordDialog
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.snackBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setOnClickButtons()
    }

    private fun setOnClickButtons(){
        textViewSignUp.setOnClickListener { goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        textViewForgotPassword.setOnClickListener {
            ForgotPasswordDialog().show(supportFragmentManager, "")
        }
        buttonSignInWithEmailAndPassword.setOnClickListener {
            var email = textInputEmail.text.toString()
            var password = textInputPassword.text.toString()
            signInWithEmailAndPassword(email,password)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                if (mAuth.currentUser!!.isEmailVerified){
                    goToActivity<MainActivity>{
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                } else {
                   snackBar(getString(R.string.snackbar_confirm_email)+" "+mAuth.currentUser!!.email, view = findViewById(R.id.activity_sign_in), action = "Resend"){
                       mAuth.currentUser!!.sendEmailVerification()
                   }
                }
            } else {
                snackBar(getString(R.string.snackbar_need_register))
            }
        }
    }
}