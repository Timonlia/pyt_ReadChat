package com.albertomoya.readchat.utilities.providers

import android.content.Context
import com.albertomoya.readchat.utilities.CompressorBitmapImage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*

class ImageProvider {
    private var mStorage:StorageReference = FirebaseStorage.getInstance().reference

   fun saveImage(context: Context, file: File): UploadTask{
        var imageByte = CompressorBitmapImage.getImage(context,file.path, 500, 500)
        val storage = mStorage.child(Date().toString() + ".jpg")
        mStorage = storage
        return storage.putBytes(imageByte)
   }

    fun getStorage(): StorageReference{
        return mStorage
    }
}