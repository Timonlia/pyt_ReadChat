package com.albertomoya.readchat.activities.others

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.login.SignInActivity
import com.albertomoya.readchat.activities.main.MainActivity
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.others.onChange
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.FileUtil
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.ImageProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.lang.Exception

class EditProfileActivity : AppCompatActivity() {
    // Variables
    private val dbProvider = AuthProvider()
    private val usrProvider = UsersProvider()

    private val mStorage = ImageProvider()
    private val  REQUEST_CODE_GALLERY = 1
    private lateinit var fImage: File

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
        imageEditProfile.setOnClickListener {
            openGalleryToChoseImage()
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
        usrProvider.updateUserProfile(user).addOnCompleteListener {
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
                val urlPhotoProfile = it[NamesCollection.COLLECTION_USER_PHOTO_URL].toString()
                Glide
                    .with(this)
                    .load(urlPhotoProfile)
                    .circleCrop()
                    .override(350, 350)
                    .into(imageEditProfile)
                isSave = true
            }
        }
    }

    private fun finishActivityIfIsSave(){
        if (isSave) {
            finish()
        } else {
            snackBar(
                "Guarda los cambios para salir",
                view = findViewById(R.id.activity_edit_profile)
            )
        }
    }

    private fun saveImageProfile(){
        mStorage.saveImage(this,fImage).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful){
                mStorage.getStorage().downloadUrl.addOnSuccessListener { it1 ->
                    val uri = it1.toString()
                    updatePhotoUrl(uri)
                }
                Log.i("IMAGE SAVE","Imagen guardada correctamente")
            } else {
                Log.i("IMAGE SAVE","No se ha guardado la imagen")
            }
        })
    }

    private fun updatePhotoUrl(photoURL: String){
        val user = User()
        user.UID = dbProvider.getUid().toString()
        user.photoUrl = photoURL
        usrProvider.updateUserPhoto(user).addOnCompleteListener {

        }
    }

    private fun openGalleryToChoseImage(){
        val openGallery = Intent(Intent.ACTION_GET_CONTENT)
        openGallery.type = "image/*"
        startActivityForResult(openGallery,REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_GALLERY -> {
                try {
                    fImage = FileUtil.from(this,data!!.data)
                    imageEditProfile.setImageBitmap(BitmapFactory.decodeFile(fImage.absolutePath))
                    saveImageProfile()
                }
                catch (e: Exception) {
                    Log.e("GALERIA","Error al abrir galeria")
                }
            }
        }
    }

    override fun onBackPressed() {
        finishActivityIfIsSave()
    }

}