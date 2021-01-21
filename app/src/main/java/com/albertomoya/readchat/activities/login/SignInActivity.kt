package com.albertomoya.readchat.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.albertomoya.readchat.activities.main.MainActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.dialogs.ForgotPasswordDialog
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.others.toast
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlin.collections.HashMap


class SignInActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mGoogleSignInClient: GoogleSignInClient by lazy { getGoogleApiClient() }
    private val mDb = FirebaseFirestore.getInstance()
    // Variables
    private val codeARGoogleSignIn = 1
    private val newUser: Map<User, Any> = HashMap()
    private val dbProvider = AuthProvider()
    private val usrProvider = UsersProvider()
    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setOnClickButtons()
    }

    // onClickButtons
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
            if (!email.isNullOrEmpty() || !password.isNullOrEmpty())
                signInWithEmailAndPassword(email, password)
        }
        buttonSignInWithGoogle.setOnClickListener {
            startActivityForResult(mGoogleSignInClient.signInIntent, codeARGoogleSignIn)
        }
    }

    // Create account with email and password
    private fun signInWithEmailAndPassword(email: String, password: String){
        dbProvider.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                if (mAuth.currentUser!!.isEmailVerified){
                    goToActivity<MainActivity>{
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                    )
                } else {
                   toast(getString(R.string.snackbar_confirm_email))
                }
            } else {
                toast(getString(R.string.snackbar_need_register))
            }
        }
    }

    // Build the SignInClient
    private fun getGoogleApiClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    // Create account with Google
    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if (GoogleSignIn.getLastSignedInAccount(this) == null){
                startActivityForResult(mGoogleSignInClient.signInIntent, codeARGoogleSignIn)
            } else {
                searchIfDataUserIsIntoDatabase()
                goToActivity<MainActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                finish()
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )

            }
        }
    }
    // Create register into Database to new User
    private fun createNewUserIntoDatabaseWithFirestore(email: String, user: String, uid: String, photoUrl: String){
        val newUser = User()
        newUser.nick = user
        newUser.email = email
        newUser.UID = uid
        newUser.photoUrl = photoUrl
        usrProvider.createUser(newUser).addOnSuccessListener { Log.i("Guardado","Se ha creado usuario en base de datos") }
    }

    // void to search into database if exists current user into database
    private fun searchIfDataUserIsIntoDatabase(){
        usrProvider.getUser(dbProvider.getUid().toString()).addOnSuccessListener{
            if (!it.exists()){
                createNewUserIntoDatabaseWithFirestore(dbProvider.getEmail().toString(),dbProvider.getDisplayName().toString(),dbProvider.getUid().toString(),dbProvider.getPhotoUrl().toString())
            }
        }
    }


    // onResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            codeARGoogleSignIn -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result != null) {
                    if (result.isSuccess) {
                        val account = result.signInAccount
                        loginByGoogleAccountIntoFirebase(account!!)

                    }
                }
            }
        }
    }
}