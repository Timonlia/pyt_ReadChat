package com.albertomoya.readchat.activities.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.snackBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonSignUp.setOnClickListener {
            var email = textInputEmail.text.toString()
            var password = textInputPassword.text.toString()
            var user = textInputUser.text.toString()
            signUpWithEmailAndPassword(email,password)
        }

    }

    private fun signUpWithEmailAndPassword(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                    snackBar(getString(R.string.snackbar_email_confirm_send), view = findViewById(R.id.activity_sign_up))
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        goToActivity<SignInActivity>{
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    },2000)
                }
            } else {
                snackBar(getString(R.string.snackbar_unexpected_error_register), view = findViewById(R.id.activity_sign_up))
            }
        }
    }
}