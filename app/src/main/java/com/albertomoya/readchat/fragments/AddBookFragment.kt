package com.albertomoya.readchat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.albertomoya.readchat.R
import com.albertomoya.readchat.activities.others.BookDetailAndEditActivity
import com.albertomoya.readchat.others.snackBar
import com.albertomoya.readchat.persistance.*
import com.albertomoya.readchat.utilities.FileUtil
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.fragment_add_book.*
import kotlinx.android.synthetic.main.fragment_add_book.view.*
import java.io.File
import java.lang.Exception


class AddBookFragment : Fragment() {

    // Variables
    private val arrayCategories = ArrayList<String>()
    private val mAuth = AuthProvider()
    private val mStorage = ImageProvider()
    private val addBook = BookProvider()
    private val usersProvider = UsersProvider()
    private val REQUEST_CODE_GALLERY = 1
    private lateinit var fImage: File
    private var uriImageBook: String = ""
    private lateinit var rootView: View
    private val chatProvider = ChatProvider()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_book, container, false)
        setUpSpinner()
        onClicksButtonView()
        activity!!.title = activity!!.applicationContext.getString(R.string.title_add_post_book)
        return rootView
    }

    private fun onClicksButtonView(){
        rootView.imageCreateBook.setOnClickListener {
            openGalleryToChoseImage()
        }
        rootView.buttonAddBook.setOnClickListener {
            if (validateEditText())
                createBook()
            else
                activity!!.snackBar("Porfavor rellena los campos 'Title' y 'Description'")
        }
    }

    private fun setUpSpinner(){
        arrayCategories.add("Ciencia Ficcion")
        arrayCategories.add("Fantasia")
        arrayCategories.add("Novela")
        arrayCategories.add("Aventura")
        arrayCategories.add("Humor")
        arrayCategories.add("Autoayuda")
        arrayCategories.add("Autobiográficos")

        val adapter = ArrayAdapter(rootView.context,android.R.layout.simple_spinner_item,arrayCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView.spinnerSearchableCategory.adapter = adapter
    }

    private fun createBook(){
        val newBook = Book()
        val title = rootView.editTextTitleCreateBook.text
        val description = rootView.editTextDescriptionCreateBook.text
        val category = rootView.spinnerSearchableCategory.selectedItem
        val isChat = rootView.checkBoxChat.isChecked
        newBook.titleBook = title.toString()
        newBook.uidAuthor = mAuth.getUid().toString()
        newBook.UID = newBook.uidAuthor+"_"+newBook.titleBook
        newBook.emailAuthor = mAuth.getEmail().toString()
        newBook.categoryBook = category.toString()
        newBook.descriptionBook = description.toString()
        newBook.chatBook = isChat
        newBook.photoBook = uriImageBook
        newBook.UIDChat = "Chat_"+newBook.titleBook+"_"+newBook.emailAuthor

        if (isChat){
            val chat = Chat()
            chat.uidChat = newBook.UIDChat
            chat.photoChat = newBook.photoBook
            chat.nameChat = newBook.titleBook
            chatProvider.createChat(chat)
        }

        try {
            addBook.createBook(newBook).addOnCompleteListener {
                if (it.isSuccessful){

                    activity!!.snackBar("Libro creado correctamente")

                    val favBook = FavouriteBook()
                    favBook.uidFavChatBook = newBook.UIDChat
                    favBook.uidFavBook = newBook.UID
                    usersProvider.addFavouriteBook(mAuth.getUid().toString(),favBook)

                    val chatUser = ChatUser()
                    if (newBook.chatBook) {
                        chatUser.uidChat =
                            newBook.UIDChat
                        chatUser.uidUser = mAuth.getUid().toString()
                        usersProvider.addChatBookFavourite(chatUser)
                        chatProvider.addUserToChat(chatUser.uidChat, chatUser)
                    }

                    val intent = Intent(rootView.context, BookDetailAndEditActivity::class.java)
                    intent.putExtra("id",newBook.UID)
                    rootView.editTextTitleCreateBook.setText("")
                    rootView.editTextDescriptionCreateBook.setText("")
                    rootView.checkBoxChat.isChecked = false
                    imageCreateBook.setImageDrawable(rootView.context.getDrawable(R.drawable.background_header))
                    startActivity(intent)
                }
            }

        }catch (e:Exception){
            activity!!.snackBar("Error al crear libro, intentelo mas tarde")
        }



        updateDatabaseCurrentUser()
    }

    private fun updateDatabaseCurrentUser(){
        val user = User()
        user.UID = mAuth.getUid().toString()
        usersProvider.getUser(mAuth.getUid().toString()).addOnSuccessListener {
            if (it.exists()) {
                user.quantityBooksUserCreated = it[NamesCollection.COLLECTION_USER_QUANTITY_BOOKS_USER_CREATE].toString().toInt()+1
                Log.i("BOOKS",user.quantityBooksUserCreated.toString())
                usersProvider.updateUserBooks(user).addOnCompleteListener {

                }
            }
        }
    }

    private fun openGalleryToChoseImage(){
        val openGallery = Intent(Intent.ACTION_GET_CONTENT)
        openGallery.type = "image/*"
        startActivityForResult(openGallery,REQUEST_CODE_GALLERY)
    }

    private fun saveImageBook(){
        mStorage.saveImage(rootView.context,fImage).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful){
                mStorage.getStorage().downloadUrl.addOnSuccessListener { it1 ->
                    uriImageBook = it1.toString()
                    Log.i("IMAGEN_URL_book",uriImageBook)
                }
                Log.i("IMAGE SAVE","Imagen guardada correctamente")
            } else {
                Log.i("IMAGE SAVE","No se ha guardado la imagen")
            }
        })
    }

    private fun validateEditText(): Boolean{
        val title = rootView.editTextTitleCreateBook.text
        val description = rootView.editTextDescriptionCreateBook.text
        return title.isNotEmpty() && description.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_GALLERY -> {
                try {
                    fImage = FileUtil.from(rootView.context,data!!.data)
                    //rootView.imageCreateBook.setImageBitmap(BitmapFactory.decodeFile(fImage.absolutePath))
                    Glide
                        .with(this)
                        .load(fImage.absoluteFile)
                        .override(600,600)
                        .into(rootView.imageCreateBook)
                    saveImageBook()
                }
                catch (e: Exception) {
                    Log.e("GALERIA","Error al abrir galeria")
                }
            }
        }
    }
}