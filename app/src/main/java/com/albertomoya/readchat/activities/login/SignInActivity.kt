package com.albertomoya.readchat.activities.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.dialogs.ForgotPasswordDialog
import com.albertomoya.readchat.others.goToActivity
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setOnClickButtons()
        setDimensionsButtonSignInWithGoogle()
    }

    private fun setOnClickButtons(){
        textViewSignUp.setOnClickListener { goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        textViewForgotPassword.setOnClickListener {
            ForgotPasswordDialog().show(supportFragmentManager,"")
            /*goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)*/

        }
    }

    private fun setDimensionsButtonSignInWithGoogle(){
        buttonSignInWithGoogle.setSize(SignInButton.SIZE_STANDARD)
    }
}