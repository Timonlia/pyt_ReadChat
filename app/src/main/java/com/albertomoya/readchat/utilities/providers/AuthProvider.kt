package com.albertomoya.readchat.utilities.providers

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthProvider {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // SignIn
    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }
    // SignUp
    fun signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email,password)
    }
    // Uid
    fun getUid(): String? {
        return if (mAuth.currentUser != null)
            mAuth.currentUser!!.uid
        else
            null
    }
    // Email
    fun getEmail(): String? {
        return if (mAuth.currentUser != null)
            mAuth.currentUser!!.email
        else
            null
    }
    // DisplayName
    fun getDisplayName(): String? {
        return if (mAuth.currentUser != null)
            mAuth.currentUser!!.displayName
        else
            null
    }
    // PhotoUrl
    fun getPhotoUrl(): Uri? {
        return if (mAuth.currentUser != null)
            mAuth.currentUser!!.photoUrl
        else
            null
    }
}