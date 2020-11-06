package com.albertomoya.readchat.activities.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.*
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.textInputEmail
import kotlinx.android.synthetic.main.activity_sign_up.textInputPassword
import kotlin.collections.hashMapOf as hashMapOf

class SignUpActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()
    // Variables
    private lateinit var newUser: User
    private val dbProvider = AuthProvider()
    private val usrProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Validaciones
        validateTextView()
        // Boton registrar
        buttonSignUp.setOnClickListener {
            val email = textInputEmail.text.toString()
            val password = textInputPassword.text.toString()
            val user = textInputUser.text.toString()
            if (user.isNotEmpty()) signUpWithEmailAndPassword(email,password,user) else snackBar("Rellena el campo usuario")
        }
        
    }

    private fun validateTextView(){
        textInputEmail.onChange {
            textInputEmail.error = if (isValidEmail(it)) null else getString(R.string.error_email_is_not_valid)
        }
        textInputPassword.onChange {
            textInputPassword.error = if (isValidPassword(it)) null else getString(R.string.error_password_is_not_valid)
        }
        textInputConfirmPassword.onChange {
            textInputConfirmPassword.error = if (isValidConfirmPassword(textInputPassword.text.toString(),it)) null else getString(R.string.error_confirm_password_is_not_valid)
        }
    }

    private fun signUpWithEmailAndPassword(email: String, password: String, user: String){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                createNewUserIntoDatabaseWithFirestore(email,user,dbProvider.getUid().toString())
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

    private fun createNewUserIntoDatabaseWithFirestore(email: String, user: String, uid: String){
        val newUser = User()
        newUser.nick = user
        newUser.email = email
        newUser.UID = uid
        usrProvider.createUser(newUser).addOnSuccessListener { Log.i("Guardado","Se ha creado usuario en base de datos") }
    }
}