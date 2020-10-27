package com.albertomoya.readchat.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.albertomoya.readchat.MainActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.dialogs.ForgotPasswordDialog
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*
import kotlin.collections.HashMap


class SignInActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mGoogleSignInClient: GoogleSignInClient by lazy { getGoogleApiClient() }
    private val mDb = FirebaseFirestore.getInstance()
    // Variables
    private val codeARGoogleSignIn = 1
    private val newUser: Map<User, Any> = HashMap()
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
            signInWithEmailAndPassword(email, password)
        }
        buttonSignInWithGoogle.setOnClickListener {
            startActivityForResult(mGoogleSignInClient.signInIntent, codeARGoogleSignIn)
        }
    }

    // Create account with email and password
    private fun signInWithEmailAndPassword(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
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
                   snackBar(
                       getString(R.string.snackbar_confirm_email) + " " + mAuth.currentUser!!.email,
                       view = findViewById(
                           R.id.activity_sign_in
                       ),
                       action = "Resend"
                   ){
                       mAuth.currentUser!!.sendEmailVerification()
                   }
                }
            } else {
                snackBar(getString(R.string.snackbar_need_register))
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
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )

            }
        }
    }
    // Create register into Database to new User
    private fun createNewUserIntoDatabaseWithFirestore(email: String, user: String, uid: String){
        val newUser = User(user,email,uid)
        val hpNewUser = hashMapOf(
            NamesCollection.COLLECTION_USER_NICK to newUser.userNick,
            NamesCollection.COLLECTION_USER_EMAIL to newUser.email,
            NamesCollection.COLLECTION_USER_UID to newUser.uid,
            NamesCollection.COLLECTION_USER_FULL_NAME to newUser.fullName,
            NamesCollection.COLLECTION_USER_DATE_CREATED_ON to newUser.dateUser,
            NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE to newUser.quantityBooksUserCreate
        )

        mDb.collection(NamesCollection.COLLECTION_USER)
            .add(hpNewUser)
            .addOnSuccessListener { Log.i("Guardado","Se ha creado usuario en base de datos") }
    }

    // void to search into database if exists current user into database
    private fun searchIfDataUserIsIntoDatabase(){
        mDb.collection(NamesCollection.COLLECTION_USER)
            .whereEqualTo(NamesCollection.COLLECTION_USER_EMAIL, mAuth.currentUser!!.email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty){
                    createNewUserIntoDatabaseWithFirestore(mAuth.currentUser!!.email!!,mAuth.currentUser!!.displayName!!,mAuth.currentUser!!.uid)
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