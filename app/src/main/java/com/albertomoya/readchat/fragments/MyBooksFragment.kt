package com.albertomoya.readchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.albertomoya.readchat.R


class MyBooksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_books, container, false)
        return rootView
    }
}