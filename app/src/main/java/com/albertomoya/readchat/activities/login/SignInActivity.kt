package com.albertomoya.readchat.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albertomoya.readchat.MainActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.dialogs.ForgotPasswordDialog
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.snackBar
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mGoogleSignInClient: GoogleSignInClient by lazy { getGoogleApiClient() }

    // Variables
    private val codeARGoogleSignIn = 1

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
            val email = textInputEmail.text.toString()
            val password = textInputPassword.text.toString()
            signInWithEmailAndPassword(email,password)
        }
        buttonSignInWithGoogle.setOnClickListener {
            startActivityForResult(mGoogleSignInClient.signInIntent, codeARGoogleSignIn)
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

    // Inicio de sesion con Google
    private fun getGoogleApiClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    // Crear cuenta con FirebaseAuth unido a Google
    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if (GoogleSignIn.getLastSignedInAccount(this) == null){
                startActivityForResult(mGoogleSignInClient.signInIntent, codeARGoogleSignIn)
            } else {
                goToActivity<MainActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

    // Validaciones


    // onResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            codeARGoogleSignIn -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result != null) {
                    if (result.isSuccess){
                        val account = result.signInAccount
                        loginByGoogleAccountIntoFirebase(account!!)
                    }
                }
            }
        }
    }
}