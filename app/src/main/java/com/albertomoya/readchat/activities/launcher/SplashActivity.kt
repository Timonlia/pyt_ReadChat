package com.albertomoya.readchat.activities.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.albertomoya.readchat.MainActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.login.SignInActivity
import com.albertomoya.readchat.others.goToActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    // Handler
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Autentificador de Firebase
        val mAuth = FirebaseAuth.getInstance()
        // Animaciones del splash
        val animation_arriba = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_hacia_derecha)
        val animation_abajo = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_hacia_abajo)
        mAuth.signOut()
        imageViewLogoReadChat.startAnimation(animation_abajo)
        textViewAppNameSplashScreen.startAnimation(animation_arriba)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (mAuth.currentUser == null){
                goToActivity<SignInActivity>{
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                }
            } else {
                goToActivity<MainActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            finish()
        },5000)
    }
}


