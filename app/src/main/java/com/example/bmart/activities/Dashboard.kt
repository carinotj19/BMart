package com.example.bmart.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bmart.R
import com.example.bmart.fragments.Cart
import com.example.bmart.fragments.Home
import com.example.bmart.fragments.Messages
import com.example.bmart.fragments.Search
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Dashboard : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var profilePicture: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val user = auth.currentUser
        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.green_200))

        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.green_700))
        }
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.hamburgerNavigationView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val headerView = navigationView.getHeaderView(0)
        usernameTextView = headerView.findViewById(R.id.header_username)
        emailTextView = headerView.findViewById(R.id.header_email)
        phoneNumberTextView = headerView.findViewById(R.id.header_phone_number)
        profilePicture = headerView.findViewById(R.id.profile_picture)

        if (user != null) {
            val userId = user.uid
            val userDocRef = firestore.collection("Users").document(userId)
            val profileRef: StorageReference = storageReference.child("users/${auth.currentUser?.uid}/profile.jpg")
            profileRef.downloadUrl.addOnSuccessListener{
                Picasso.get().load(it).into(profilePicture)
            }

            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("Username")
                        val email = documentSnapshot.getString("Email")
                        val phoneNumber = documentSnapshot.getString("PhoneNumber")
                        // Update header views
                        usernameTextView.text = username
                        emailTextView.text = email
                        phoneNumberTextView.text = phoneNumber
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Toast.makeText(this@Dashboard, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
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
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
            R.id.profile -> {
                val intent = Intent(applicationContext, Profile::class.java)
                startActivity(intent)
                finish()
            }
            R.id.favourites -> {
                Toast.makeText(applicationContext, "Clicked favourites", Toast.LENGTH_SHORT).show()
            }
            R.id.orders -> {
                Toast.makeText(applicationContext, "Clicked orders", Toast.LENGTH_SHORT).show()
            }
            R.id.help -> {
                Toast.makeText(applicationContext, "Clicked help", Toast.LENGTH_SHORT).show()
            }
            R.id.privacy -> {
                Toast.makeText(applicationContext, "Clicked privacy", Toast.LENGTH_SHORT).show()
            }
            R.id.terms -> {
                Toast.makeText(applicationContext, "Clicked terms", Toast.LENGTH_SHORT).show()
            }
            R.id.logOut -> {
                Toast.makeText(applicationContext, "Clicked Log out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, Login::class.java)
                startActivity(intent)
                finish()
            }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

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

    @Deprecated("Deprecated in Java")
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