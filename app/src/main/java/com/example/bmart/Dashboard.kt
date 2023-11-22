package com.example.bmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bmart.fragments.Cart
import com.example.bmart.fragments.Home
import com.example.bmart.fragments.Messages
import com.example.bmart.fragments.Search
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.hamburgerNavigationView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.nav_bar_open,
            R.string.nav_bar_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(Home())
            bottomNavigationView.selectedItemId = R.id.home
        }
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(Home())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    replaceFragment(Search())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cart -> {
                    replaceFragment(Cart())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.messages -> {
                    replaceFragment(Messages())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
        super.onBackPressed()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()

        if (fragment is Home) {
            supportActionBar?.show()
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            supportActionBar?.hide()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.favourites -> {
                Toast.makeText(applicationContext, "Clicked favourites", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.orders -> {
                Toast.makeText(applicationContext, "Clicked orders", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.help -> {
                Toast.makeText(applicationContext, "Clicked help", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.privacy -> {
                Toast.makeText(applicationContext, "Clicked privacy", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.terms -> {
                Toast.makeText(applicationContext, "Clicked terms", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.logOut -> {
                Toast.makeText(applicationContext, "Clicked logOut", Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val receivedData = data?.getStringExtra("fragment")

            if (receivedData == "cart"){
                replaceFragment(Cart())
            }
            if (receivedData == "home"){
                replaceFragment(Home())
            }
            if (receivedData == "Messages"){
                replaceFragment(Messages())
            }
        }
    }
}