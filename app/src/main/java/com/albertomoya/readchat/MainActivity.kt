package com.albertomoya.readchat




import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.dialogs.LogoutDialog
import com.albertomoya.readchat.fragments.*
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_profile_toolbar.view.*
import kotlinx.android.synthetic.main.nav_header.view.*


class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Firebase
        // Autentificador
        private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // SetUps
        toolbarToLoad(toolbar as Toolbar)
        setNavDrawer()
        fragmentTransaction(HomeFragment())
        setUpHeaderInformation()
        navView.menu.getItem(0).isChecked = true
    }

    private fun fragmentTransaction(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun loadFragmentById(id: Int){
        when(id){
            R.id.nav_home -> fragmentTransaction(HomeFragment())
            R.id.nav_my_books -> fragmentTransaction(MyBooksFragment())
            R.id.nav_create_book -> fragmentTransaction(AddBookFragment())
            R.id.nav_chats -> fragmentTransaction(ChatFragment())
            R.id.nav_profile -> fragmentTransaction(ProfileFragment())
            R.id.nav_logout -> LogoutDialog().show(supportFragmentManager, "")
        }
    }

    private fun setUpHeaderInformation(){
        val email = navView.getHeaderView(0).textViewEmailNavHeader
        val name = navView.getHeaderView(0).textViewNameNavHeader
        val urlPhoto = navView.getHeaderView(0).imageProfile
        val displayName = mAuth.currentUser!!.displayName
        val urlPhotoProfile = mAuth.currentUser!!.photoUrl
        email?.let { email.text =  mAuth.currentUser!!.email}

        if (!displayName.isNullOrEmpty()){
            name.text = displayName
        } else {
            name.setTextColor(Color.RED)
        }
        if (urlPhotoProfile != null){
            Glide
                .with(this)
                .load(mAuth.currentUser!!.photoUrl)
                .circleCrop()
                .override(100,100)
                .into(urlPhoto)
        } else {
            Glide
                .with(this)
                .load(R.drawable.ic_person_white)
                .circleCrop()
                .override(100,100)
                .into(urlPhoto)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_options, menu)

        val menuItem = menu!!.findItem(R.id.item_profile)
        val view: View = MenuItemCompat.getActionView(menuItem)
        // val view: View = menuItem.actionView
        val profileImage: CircleImageView = view.findViewById(R.id.toolbar_profile_image)
        val urlPhotoProfile = mAuth.currentUser!!.photoUrl
        if (urlPhotoProfile != null){
            Glide
                .with(this)
                .load(mAuth.currentUser!!.photoUrl)
                .into(profileImage)
        }else{
            Glide
                .with(this)
                .load(R.drawable.ic_person_white)
                .into(profileImage)
        }

        profileImage.setOnClickListener { fragmentTransaction(ProfileFragment()) }

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_profile -> fragmentTransaction(ProfileFragment())
            // R.id.menu_three ->
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            LogoutDialog().show(supportFragmentManager, "")
        // super.onBackPressed()
    }
}