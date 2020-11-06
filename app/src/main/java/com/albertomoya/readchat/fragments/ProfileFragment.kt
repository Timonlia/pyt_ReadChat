package com.albertomoya.readchat.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.albertomoya.readchat.R
import com.albertomoya.readchat.persistance.User
import com.albertomoya.readchat.utilities.NamesCollection
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    // Firebase
    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        setUpProfileView(rootView)
        return rootView
    }

    private fun setUpProfileView(view: View){
        val urlPhotoProfile = mAuth.currentUser!!.photoUrl
        val urlPhoto = view.imageProfileFragment
        if (urlPhotoProfile != null){
            Glide
                .with(this)
                .load(mAuth.currentUser!!.photoUrl)
                .circleCrop()
                .override(350, 350)
                .into(urlPhoto)
        } else {
            Glide
                .with(this)
                .load(R.drawable.ic_person_white)
                .circleCrop()
                .override(350, 350)
                .into(urlPhoto)
        }
    }

    private fun setOnClickProfileFragment(view: View){
        view.buttonEditProfile.setOnClickListener {

        }
    }

}