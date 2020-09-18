package com.albertomoya.readchat.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.goToActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setOnClickButtons()

    }


    private fun setOnClickButtons(){
        textViewRegistrase.setOnClickListener { goToActivity<SignUpActivity>() }
    }
}