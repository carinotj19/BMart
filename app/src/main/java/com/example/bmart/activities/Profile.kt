package com.example.bmart.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmart.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Profile : AppCompatActivity() {
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePhoneNumber: TextView
    private lateinit var editProfile: ImageButton
    private lateinit var editProfilePic: ImageButton
    private lateinit var profilePicture: CircleImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileName = findViewById(R.id.profile_setName)
        profileEmail = findViewById(R.id.profile_setEmail)
        profileUsername = findViewById(R.id.profile_setUserName)
        profilePhoneNumber = findViewById(R.id.profile_setPhoneNumber)
        profilePicture = findViewById(R.id.profile_image)

        editProfile = findViewById(R.id.edit_btn)
        editProfilePic = findViewById(R.id.edit_profile_pic)

        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val profileRef: StorageReference = storageReference.child("users/${auth.currentUser?.uid}/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener{
            Picasso.get().load(it).into(profilePicture)
        }

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        profileName.text = ""
        profileEmail.text = ""
        profileUsername.text = ""
        profilePhoneNumber.text = ""

        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }

        editProfilePic.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1000)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                val imageUri: Uri? = data?.data

//                profilePicture.setImageURI(imageUri)
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val fileReference: StorageReference = storageReference.child("users/${auth.currentUser?.uid}/profile.jpg")
        fileReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
            fileReference.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(profilePicture)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Image not uploaded $exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAllUserData() {
        val user = auth.currentUser
        // Check if the user is authenticated
        if (user != null) {
            // Get the user document reference
            val userDocument: DocumentReference = fireStore.collection("Users").document(user.uid)

            // Retrieve user information from Firestore
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nameUser = documentSnapshot.getString("FullName")
                        val emailUser = documentSnapshot.getString("Email")
                        val usernameUser = documentSnapshot.getString("Username")
                        val phoneNumber = documentSnapshot.getString("PhoneNumber")

                        profileName.text = nameUser
                        profileEmail.text = emailUser
                        profileUsername.text = usernameUser
                        profilePhoneNumber.text = phoneNumber
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failures
                }
        }
    }

    private fun passUserData() {
        val userUsername = profileUsername.text.toString().trim()
        val usersCollection = FirebaseFirestore.getInstance().collection("Users")

        // Get the current user's UID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        usersCollection.whereEqualTo("Username", userUsername)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    for (document in querySnapshot.documents) {
                        // Check if the document belongs to the current user
                        if (document.id == currentUserUid) {
                            val fullNameFromDB = document.getString("FullName")
                            val emailFromDB = document.getString("Email")
                            val usernameFromDB = document.getString("Username")
                            val phoneNumberFromDB = document.getString("PhoneNumber")

                            // Perform actions with the user document
                            // For example, create an Intent and start an activity
                            startEditProfileActivity(fullNameFromDB, emailFromDB, usernameFromDB, phoneNumberFromDB)
                            return@addOnSuccessListener // Exit the loop once the correct document is found
                        }
                    }
                } else {
                    // User with the specified username does not exist
                    // Handle this case as needed
                }
            }
            .addOnFailureListener { e ->
                // Handle failures
            }
    }

    private fun startEditProfileActivity(fullName: String?, email: String?, username: String?, phoneNumber: String?) {
        val intent = Intent(this, EditProfile::class.java)
        intent.putExtra("fullName", fullName)
        intent.putExtra("email", email)
        intent.putExtra("username", username)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
        finish()
    }



}