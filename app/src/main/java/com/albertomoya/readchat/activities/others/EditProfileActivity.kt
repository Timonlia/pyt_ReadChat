package com.albertomoya.readchat.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.main.MainActivity
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.onChange
import com.albertomoya.readchat.others.snackBar
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private var isSave = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        onClickButtonsEditProfile()
        onChangeEditText()
    }

    private fun onClickButtonsEditProfile(){
        buttonSaveChangesEditProfile.setOnClickListener {
            isSave = true
        }
        buttonBackToMain.setOnClickListener {
            if (isSave)
                finish()
            else
                snackBar("Guarda los cambios para salir" ,view = findViewById(R.id.activity_edit_profile))
        }
    }

    private fun onChangeEditText(){
        editTextEditProfileNickname.onChange { isSave = false }
        editTextEditProfileDescription.onChange { isSave = false }
        editTextEditProfileFullName.onChange { isSave = false }
    }

    override fun onBackPressed() {
        if (isSave)
            super.onBackPressed()
        else
            snackBar("Guarda los cambios para salir",view = findViewById(R.id.activity_edit_profile))

    }

}