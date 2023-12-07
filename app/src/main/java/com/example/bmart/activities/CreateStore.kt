package com.example.bmart.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.example.bmart.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.security.MessageDigest


class CreateStore : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextBirthday: TextInputEditText

    private lateinit var imageViewPermit: ImageView
    private lateinit var imageViewProfile: ImageView

    private lateinit var uploadPermitButton: Button
    private lateinit var uploadProfileButton: Button

    private lateinit var uploadPermitText: TextView
    private lateinit var uploadProfileText: TextView

    private lateinit var buttonNext1: Button
    private lateinit var buttonNext2: Button
    private lateinit var buttonNext3: Button
    private lateinit var buttonNext4: Button
    private lateinit var buttonAccept: Button

    private lateinit var buttonBack: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var errorTextView: TextView

    private lateinit var viewFlipper: ViewFlipper

    private var finalEmail: String = ""
    private var finalPassword: String = ""
    private var finalName: String = ""
    private var finalBirthDate: String = ""
    private var finalPermitUrl: String = ""
    private var finalProfileUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_store)

        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        editTextEmail = findViewById(R.id.store_email)
        editTextPassword = findViewById(R.id.store_password)
        editTextName = findViewById(R.id.store_name)
        editTextBirthday = findViewById(R.id.selectBirthdate)

        imageViewPermit = findViewById(R.id.upload_permit)
        imageViewProfile = findViewById(R.id.upload_profile)

        uploadPermitButton = findViewById(R.id.upload_permit_button)
        uploadProfileButton = findViewById(R.id.upload_profile_button)

        uploadPermitText = findViewById(R.id.permit_image_text)
        uploadProfileText = findViewById(R.id.profile_image_text)

        buttonNext1 = findViewById(R.id.next_button1)
        buttonNext2 = findViewById(R.id.next_button2)
        buttonNext3 = findViewById(R.id.next_button3)
        buttonNext4 = findViewById(R.id.next_button4)
        buttonAccept = findViewById(R.id.accept_button)

        buttonBack = findViewById(R.id.back_button)

        progressBar = findViewById(R.id.progress_bar)
        progressText = findViewById(R.id.progress_text)
        errorTextView = findViewById(R.id.error_text)

        viewFlipper = findViewById(R.id.viewFlipper)

        buttonBack.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragnent", "home")
            startActivity(intent)
            finish()
        }

        uploadPermitButton.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1001)
        }

        uploadProfileButton.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1002)
        }

        buttonNext1.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            editTextEmail.isEnabled = false
            editTextPassword.isEnabled = false

            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()

            if (validation1(email, password)) {
                finalEmail = email
                finalPassword = password
                // Proceed to Step 2
                viewFlipper.showNext()
                progressBar.visibility = View.GONE
                errorTextView.text = ""
                editTextEmail.isEnabled = true
                editTextPassword.isEnabled = true
                buttonNext1.isEnabled = true
            }

            progressBar.visibility = View.GONE
            editTextEmail.isEnabled = true
            editTextPassword.isEnabled = true
        }

        buttonNext2.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            editTextName.isEnabled = false
            editTextBirthday.isEnabled = false

            val name: String = editTextName.text.toString()
            val birthday: String = editTextBirthday.text.toString()

            if (validation2(name, birthday)) {
                finalName = name
                finalBirthDate = birthday
                // Proceed to Step 2
                viewFlipper.showNext()
                progressBar.visibility = View.GONE
                errorTextView.text = ""
                editTextName.isEnabled = true
                editTextBirthday.isEnabled = true
                buttonNext2.isEnabled = true
            }

            progressBar.visibility = View.GONE
            editTextName.isEnabled = true
            editTextBirthday.isEnabled = true
        }

        buttonNext3.setOnClickListener {
            if (validation4(finalPermitUrl)){
                errorTextView.visibility = View.GONE
                viewFlipper.showNext()
            } else {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "Business Permit is required"
            }
        }

        buttonNext4.setOnClickListener {
            if (validation3(finalProfileUrl)){
                errorTextView.visibility = View.GONE
                viewFlipper.showNext()
            } else {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "Profile image is required"
            }
        }

        buttonAccept.setOnClickListener {
            Toast.makeText(this, "Terms accepted", Toast.LENGTH_SHORT).show()

            progressBar.visibility = View.VISIBLE
            progressText.visibility = View.VISIBLE
            progressText.text = "Creating Account"
            viewFlipper.visibility = View.GONE

            virtualStoreCreation()

            // Delay for 2-3 seconds before changing the text
            Handler(Looper.getMainLooper()).postDelayed({
                progressText.text = "Verifying Data"
            }, 2000) // 2 seconds

            // Delay for another 2-3 seconds before moving to a new activity
            Handler(Looper.getMainLooper()).postDelayed({
                // Start a new activity here
                val intent = Intent(this, MyStore::class.java)
                progressBar.visibility = View.GONE
                progressText.visibility = View.GONE
                startActivity(intent)
                finish()
            }, 4000)
        }
    }

    private fun virtualStoreCreation() {
        // Accessing the current user's UID
        val userId = auth.currentUser?.uid

        // Check if the user is authenticated
        if (userId != null) {
            // Reference to the Users collection
            val usersCollection = fireStore.collection("Users")

            // Create a new document for the user (Firestore will generate a unique ID)
            val userDocument = usersCollection.document(userId)

            // Reference to the VirtualStore sub-collection within the user's document
            val virtualStoreCollection = userDocument.collection("VirtualStore")

            // Create a new document inside the VirtualStore collection (Firestore will generate a unique ID)
            val virtualStoreDocument = virtualStoreCollection.document()

            val newPass =  sha256(finalPassword).toString();

            // Add data to the VirtualStore document
            virtualStoreDocument.set(
                mapOf(
                    "email" to finalEmail,
                    "password" to newPass,
                    "storeName" to finalName,
                    "birthDate" to finalBirthDate,
                    "permit" to finalPermitUrl,
                    "storePicture" to finalProfileUrl
                )
            ).addOnSuccessListener {
                // Data added successfully
                Toast.makeText(this, "VirtualStore created for the user", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { e ->
                // Handle any errors that occurred during data addition
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sha256(base: String): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(base.toByteArray(charset("UTF-8")))
            val hexString = StringBuffer()
            for (i in hash.indices) {
                val hex = Integer.toHexString(0xff and hash[i].toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    private fun validation1(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = "All fields are required"
            return false
        }
        if (password.length < 8) {
            Toast.makeText(this, "Password Must be 8 character long", Toast.LENGTH_SHORT).show()
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = "Password Must be 8 character long"
            return false
        }
        errorTextView.visibility = View.GONE
        return true
    }

    private fun validation2(name: String, birthday: String): Boolean {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(birthday)) {
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = "All fields are required"
            return false
        }
        errorTextView.visibility = View.GONE
        return true
    }

    private fun validation3(profileImageUrl: String): Boolean {
        if (TextUtils.isEmpty(profileImageUrl)) {
            return false
        }
        finalProfileUrl = profileImageUrl
        return true
    }

    private fun validation4(permitImageUrl: String): Boolean {
        if (TextUtils.isEmpty(permitImageUrl)) {
            return false
        }
        finalPermitUrl = permitImageUrl
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001){
            if (resultCode == Activity.RESULT_OK){
                val imageUri: Uri? = data?.data
                imageViewPermit.setImageURI(imageUri)
                if (imageUri != null) {
                    progressBar.visibility = View.VISIBLE
                    uploadPermitText.visibility = View.GONE
                    uploadImageToFirebase(imageUri, "permit")
                    progressBar.visibility = View.GONE
                }
            }
        }
        if (requestCode == 1002){
            if (resultCode == Activity.RESULT_OK){
                val imageUri: Uri? = data?.data
                imageViewProfile.setImageURI(imageUri)
                uploadPermitText.visibility = View.GONE
                if (imageUri != null) {
                    progressBar.visibility = View.VISIBLE
                    uploadProfileText.visibility = View.GONE
                    uploadImageToFirebase(imageUri, "profile")
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, type: String) {
        val permitReference: StorageReference = storageReference.child("vendors/${auth.currentUser?.uid}/vendorPermit.jpg")
        val profileReference: StorageReference = storageReference.child("vendors/${auth.currentUser?.uid}/vendorProfile.jpg")
        if (type == "permit"){
            permitReference.putFile(imageUri).addOnSuccessListener {
                permitReference.downloadUrl.addOnSuccessListener {
                    finalPermitUrl = it.toString()
                    Picasso.get().load(it).into(imageViewPermit)
                    progressBar.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            }.addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                uploadPermitText.visibility = View.GONE
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "Something went Wrong! :${exception}"
            }
        } else if (type == "profile") {
            profileReference.putFile(imageUri).addOnSuccessListener {
                Toast.makeText(this, "Profile image uploaded:", Toast.LENGTH_SHORT).show()
                profileReference.downloadUrl.addOnSuccessListener {
                    // Store the image URL in the variable
                    finalProfileUrl = it.toString()
                    Picasso.get().load(it).into(imageViewProfile)
                    progressBar.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            }.addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                uploadProfileText.visibility = View.GONE
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "Profile image not uploaded: $exception"
            }
        } else {
            Toast.makeText(this, "INVALID TYPE", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = "Invalid Typr"
        }
    }
}