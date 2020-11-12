package com.albertomoya.readchat.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.main.MainActivity
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.onChange
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    // Variables
    private val dbProvider = AuthProvider()
    private val usrProvider = UsersProvider()

    private var isSave = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        onClickButtonsEditProfile()
        onChangeEditText()
        setUpEditProfile()
    }

    private fun onClickButtonsEditProfile(){
        buttonSaveChangesEditProfile.setOnClickListener {
            if (!isSave) {
                updateInfoUserProfile()
                isSave = true
            }else {
                snackBar("No hay cambios que guardar" ,view = findViewById(R.id.activity_edit_profile))
            }
        }
        buttonBackToMain.setOnClickListener {
            finishActivityIfIsSave()
        }
    }

    private fun onChangeEditText(){
        editTextEditProfileNickname.onChange { isSave = false }
        editTextEditProfileDescription.onChange { isSave = false }
        editTextEditProfileFullName.onChange { isSave = false }
    }

    private fun updateInfoUserProfile(){
        val nickname = editTextEditProfileNickname.text.toString()
        val fullName = editTextEditProfileFullName.text.toString()
        val description = editTextEditProfileDescription.text.toString()
        val user = User()
            user.UID = dbProvider.getUid().toString()
            user.nick = nickname
            user.fullName = fullName
            user.descriptionUser = description
        usrProvider.updateUser(user).addOnCompleteListener {
            if (it.isSuccessful)
                snackBar("Los cambios se han guardado correctamente" ,view = findViewById(R.id.activity_edit_profile))
            else
                snackBar("No se han podido guardar los cambios, intentelo mas tarde." ,view = findViewById(R.id.activity_edit_profile))
        }
    }

    private fun setUpEditProfile(){
        usrProvider.getUser(dbProvider.getUid().toString()).addOnSuccessListener {
            if (it.exists()){
                editTextEditProfileNickname.setText(it[NamesCollection.COLLECTION_USER_NICK].toString())
                editTextEditProfileFullName.setText(it[NamesCollection.COLLECTION_USER_FULL_NAME].toString())
                editTextEditProfileDescription.setText(it[NamesCollection.COLLECTION_USER_DESCRIPTION].toString())
                isSave = true
            }
        }
    }

    private fun finishActivityIfIsSave(){
        if (isSave)
            finish()
        else
            snackBar("Guarda los cambios para salir" ,view = findViewById(R.id.activity_edit_profile))
    }

    override fun onBackPressed() {
        finishActivityIfIsSave()
    }

}