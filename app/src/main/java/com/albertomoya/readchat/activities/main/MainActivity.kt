package com.albertomoya.readchat.activities.main




import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.R
import com.albertomoya.readchat.dialogs.LogoutDialog
import com.albertomoya.readchat.fragments.*
import com.albertomoya.readchat.utilities.NamesCollection
import com.albertomoya.readchat.utilities.providers.AuthProvider
import com.albertomoya.readchat.utilities.providers.UsersProvider
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.nav_header.view.*


class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Firebase
        // Autentificador
        private val mAuth = FirebaseAuth.getInstance()
    // Variables
    private val authProvider = AuthProvider()
    private val usrProvider = UsersProvider()
    private var fragment = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = 0
        // SetUps
        toolbarToLoad(toolbar as Toolbar)
        setNavDrawer()
        fragmentTransaction(HomeFragment())
        setUpHeaderInformation()
        listenerChangesCollection()
        navView.menu.getItem(0).isChecked = true
    }

    private fun fragmentTransaction(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun loadFragmentById(id: Int){
        when(id){
            R.id.nav_home -> { fragmentTransaction(HomeFragment())
                fragment = 0 }
            R.id.nav_my_books -> { fragmentTransaction(MyBooksFragment())
                fragment = 1 }
            R.id.nav_create_book -> { fragmentTransaction(AddBookFragment())
                fragment = 1 }
            R.id.nav_fav_books -> { fragmentTransaction(FavouriteBooksFragment())
                fragment = 1 }
            R.id.nav_chats -> { fragmentTransaction(ChatFragment())
                fragment = 1 }
            R.id.nav_profile -> { fragmentTransaction(ProfileFragment())
                fragment = 1 }
            R.id.nav_logout -> LogoutDialog().show(supportFragmentManager, "")
        }
    }

    private fun setUpHeaderInformation(){
        val email = navView.getHeaderView(0).textViewEmailNavHeader
        val name = navView.getHeaderView(0).textViewNameNavHeader
        val urlPhoto = navView.getHeaderView(0).imageProfile
        var background = navView.getHeaderView(0).headerBackground
        usrProvider.getUser(authProvider.getUid().toString()).addOnSuccessListener {
            name.text = it[NamesCollection.COLLECTION_USER_NICK].toString()
            email.text = it[NamesCollection.COLLECTION_USER_EMAIL].toString()
            val urlPhotoProfile = it[NamesCollection.COLLECTION_USER_PHOTO_URL].toString()
            val urlPhotoProfileBackground = it[NamesCollection.COLLECTION_USER_PHOTO_BACKGROUND].toString()
            Glide
                .with(this)
                .load(urlPhotoProfile)
                .into(urlPhoto)
            Glide
                .with(this)
                .load(urlPhotoProfileBackground)
                .centerCrop()
                .override(400, 400)
                .into(background)
        }
    }

    private fun setNavDrawer(){
        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            _toolbar,
            R.string.nav_open_drawer,
            R.string.nav_close_drawer
        )
        toogle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragmentById(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun listenerChangesCollection(){
        usrProvider.getUserForSnapshot(authProvider.getUid().toString()).addSnapshotListener(object : java.util.EventListener,
            EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                setUpHeaderInformation()

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_options, menu)

        val menuItem = menu!!.findItem(R.id.item_profile)
        val view: View = MenuItemCompat.getActionView(menuItem)
        // val view: View = menuItem.actionView
        val profileImage: CircleImageView = view.findViewById(R.id.toolbar_profile_image)
        val doc = usrProvider.getUserForSnapshot(authProvider.getUid().toString())
        doc.addSnapshotListener(object : java.util.EventListener,
            EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                usrProvider.getUser(authProvider.getUid().toString()).addOnSuccessListener {
                    val urlPhotoProfile = it[NamesCollection.COLLECTION_USER_PHOTO_URL].toString()
                    Glide
                        .with(view.context)
                        .load(urlPhotoProfile)
                        .into(profileImage)
                }
            }
        })
        profileImage.setOnClickListener {
            navView.menu.findItem(R.id.nav_profile).isChecked = true
            navView.menu.getItem(0).isChecked = false
            fragmentTransaction(ProfileFragment())
            fragment = 1

        }

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_profile -> fragmentTransaction(ProfileFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            if (fragment == 1) {
                loadFragmentById(R.id.nav_home)
                drawerLayout.closeDrawer(GravityCompat.START)
                navView.menu.getItem(0).isChecked = true
                navView.menu.findItem(R.id.nav_profile).isChecked = false
            } else {
                LogoutDialog().show(supportFragmentManager, "")
            }
        // super.onBackPressed()

    }


}