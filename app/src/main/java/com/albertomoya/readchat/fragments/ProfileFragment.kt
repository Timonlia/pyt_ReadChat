package com.albertomoya.readchat.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.others.EditProfileActivity
import com.albertomoya.readchat.others.goToActivity
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.FileUtil
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.ImageProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.File
import java.lang.Exception
import java.util.*


class ProfileFragment : Fragment() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    // Variables
    private lateinit var rootView: View
    private val dbProvider = AuthProvider()
    private val usrProvider = UsersProvider()
    private val mStorage = ImageProvider()
    private val  IMAGE_BACKGROUND = 1
    private val  IMAGE_PROFILE = 2
    private lateinit var fImage: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        activity!!.title = activity!!.applicationContext.getString(R.string.title_my_profile)
        setOnClicksProfileFragment()
        setUpProfileFragment()
        listenerChangesCollection()
        return rootView
    }

    private fun setOnClicksProfileFragment(){
        rootView.buttonEditProfile.setOnClickListener {
            activity!!.goToActivity<EditProfileActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            activity!!.overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }
        rootView.imageProfileFragment.setOnClickListener {
            openGalleryToChoseImage(IMAGE_PROFILE)
        }
        rootView.imageProfileBackgroundFragment.setOnClickListener {
            openGalleryToChoseImage(IMAGE_BACKGROUND)
        }
    }

    private fun setUpProfileFragment(){
        usrProvider.getUser(dbProvider.getUid().toString()).addOnSuccessListener(OnSuccessListener {
            if (it.exists()) {
                rootView.textViewNickUser.text = it[NamesCollection.COLLECTION_USER_NICK].toString()
                rootView.textViewFollowers.text =
                    "${rootView.context.getString(R.string.votes)}: ${it[NamesCollection.COLLECTION_USER_QUANTITY_FOLLOWERS].toString()}"
                rootView.textViewDescriptionUser.text =
                    it[NamesCollection.COLLECTION_USER_DESCRIPTION].toString()
                rootView.textViewBooks.text =
                    "${rootView.context.getString(R.string.books)}: ${it[NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE].toString()}"
                val urlPhotoProfile = it[NamesCollection.COLLECTION_USER_PHOTO_URL].toString()
                val urlPhotoProfileBackground = it[NamesCollection.COLLECTION_USER_PHOTO_BACKGROUND].toString()
                Glide
                    .with(rootView.context)
                    .load(urlPhotoProfile)
                    .circleCrop()
                    .override(350, 350)
                    .into(rootView.imageProfileFragment)
                Glide
                    .with(rootView.context)
                    .load(urlPhotoProfileBackground)
                    .centerCrop()
                    .override(400,400)
                    .into(rootView.imageProfileBackgroundFragment)
            }
        })
    }

    private fun listenerChangesCollection(){
        usrProvider.getUserForSnapshot(dbProvider.getUid().toString()).addSnapshotListener(object : java.util.EventListener,
            EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                setUpProfileFragment()
            }
        })
    }

    private fun saveImageProfile(image: Int){
        when(image){
            IMAGE_PROFILE -> {
                mStorage.saveImage(rootView.context,fImage).addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful){
                        mStorage.getStorage().downloadUrl.addOnSuccessListener { it1 ->
                            val uri = it1.toString()
                            updatePhotoUrl(uri, IMAGE_PROFILE)
                            Log.i("IMAGEN_URL",uri)
                        }
                        Log.i("IMAGE SAVE","Imagen guardada correctamente")
                    } else {
                        Log.i("IMAGE SAVE","No se ha guardado la imagen")
                    }
                })
            }
            IMAGE_BACKGROUND -> {
                mStorage.saveImage(rootView.context,fImage).addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful){
                        mStorage.getStorage().downloadUrl.addOnSuccessListener { it1 ->
                            val uri = it1.toString()
                            updatePhotoUrl(uri, IMAGE_BACKGROUND)
                        }
                        Log.i("IMAGE SAVE","Imagen guardada correctamente")
                    } else {
                        Log.i("IMAGE SAVE","No se ha guardado la imagen")
                    }
                })
            }
        }

    }

    private fun updatePhotoUrl(photoURL: String, image: Int){
        val user = User()
        when(image){
            IMAGE_PROFILE -> {
                user.UID = dbProvider.getUid().toString()
                user.photoUrl = photoURL
                usrProvider.updateUserPhoto(user).addOnCompleteListener {}
            }
            IMAGE_BACKGROUND -> {
                user.UID = dbProvider.getUid().toString()
                user.photoBackground = photoURL
                usrProvider.updateUserPhotoBackground(user).addOnCompleteListener {}
            }
        }
    }

    private fun openGalleryToChoseImage(image: Int){
        when(image){
            IMAGE_PROFILE -> {
                val openGallery = Intent(Intent.ACTION_GET_CONTENT)
                openGallery.type = "image/*"
                startActivityForResult(openGallery,IMAGE_PROFILE)
            }
            IMAGE_BACKGROUND -> {
                val openGallery = Intent(Intent.ACTION_GET_CONTENT)
                openGallery.type = "image/*"
                startActivityForResult(openGallery,IMAGE_BACKGROUND)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_PROFILE -> {
                try {
                    fImage = FileUtil.from(rootView.context,data!!.data)
                    rootView.imageProfileFragment.setImageBitmap(BitmapFactory.decodeFile(fImage.absolutePath))
                    saveImageProfile(IMAGE_PROFILE)
                }
                catch (e: Exception) {
                    Log.e("GALERIA","Error al abrir galeria")
                }
            }
            IMAGE_BACKGROUND -> {
                try {
                    fImage = FileUtil.from(rootView.context,data!!.data)
                    rootView.imageProfileBackgroundFragment.setImageBitmap(BitmapFactory.decodeFile(fImage.absolutePath))
                    saveImageProfile(IMAGE_BACKGROUND)
                }
                catch (e: Exception) {
                    Log.e("GALERIA","Error al abrir galeria")
                }
            }
        }
    }
}