package com.albertomoya.readchat

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.albertomoya.readchat.dialogs.LogoutDialog
import com.albertomoya.readchat.fragments.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
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
            R.id.nav_logout -> LogoutDialog().show(supportFragmentManager,"")
        }
    }

    private fun setUpHeaderInformation(){
        val email = navView.getHeaderView(0).textViewEmailNavHeader
        val name = navView.getHeaderView(0).textViewNameNavHeader

        name?.let { name.text =  mAuth.currentUser!!.displayName}
        email?.let { email.text =  mAuth.currentUser!!.email}
    }

    private fun setNavDrawer(){
        val toogle = ActionBarDrawerToggle(this,drawerLayout, _toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer)
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            LogoutDialog().show(supportFragmentManager,"")
        // super.onBackPressed()
    }
}