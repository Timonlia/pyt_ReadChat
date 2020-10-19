package com.albertomoya.readchat.activities.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.textInputEmail
import kotlinx.android.synthetic.main.activity_sign_up.textInputPassword

class SignUpActivity : AppCompatActivity() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Validaciones
        validateTextView()
        // Boton registrar
        buttonSignUp.setOnClickListener {
            var email = textInputEmail.text.toString()
            var password = textInputPassword.text.toString()
            var user = textInputUser.text.toString()
            if (user.isNotEmpty()) signUpWithEmailAndPassword(email,password) else snackBar("Rellena el campo usuario")
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