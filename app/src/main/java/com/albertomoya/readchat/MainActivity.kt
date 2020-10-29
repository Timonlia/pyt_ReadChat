package com.albertomoya.readchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.albertomoya.mylibrary.activities.ToolbarActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

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
    }

    private fun setNavDrawer(){
        val toogle = ActionBarDrawerToggle(this,drawerLayout, _toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer)
        toogle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}