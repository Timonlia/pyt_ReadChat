package com.albertomoya.readchat.activities.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.login.SignInActivity
import com.albertomoya.readchat.others.goToActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    // Handler
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation_arriba = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_hacia_derecha)
        val animation_abajo = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_hacia_abajo)

        imageViewLogoReadChat.startAnimation(animation_abajo)
        textViewAppNameSplashScreen.startAnimation(animation_arriba)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            goToActivity<SignInActivity>()
        },5000)
    }
}


