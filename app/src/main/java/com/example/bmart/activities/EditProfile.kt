package com.example.bmart.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmart.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfile : AppCompatActivity() {
    private lateinit var editFullName: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: ImageButton
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var fullNameUser: String = ""
    private var emailUser: String = ""
    private var usernameUser: String = ""
    private var phoneNumberUser: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        editFullName = findViewById(R.id.profile_setName)
        editPhoneNumber = findViewById(R.id.profile_setPhoneNumber)
        editEmail = findViewById(R.id.profile_setEmail)
        editUsername = findViewById(R.id.profile_setUserName)
        editPassword = findViewById(R.id.profile_setPassword)
        saveButton = findViewById(R.id.save_btn)

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }

        showData()

        saveButton.setOnClickListener {
            val password: String = editPassword.text.toString()
            if (isFullNameChanged() || isEmailChanged() || isPhoneNumberChanged() || isUsernameChanged() || !TextUtils.isEmpty(password)) {
                changePassword(password)
                Toast.makeText(this@EditProfile, "Changes Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@EditProfile, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changePassword(password: String) {
        val user = FirebaseAuth.getInstance().currentUser

        // Re-authenticate the user
        val credential = EmailAuthProvider.getCredential(user?.email ?: "", password)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Re-authentication successful, proceed with password change
                    user.updatePassword(password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Password update successful
                                Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                // Password update failed
                                val exception = task.exception
                                // Handle exception, check for specific error codes
                                Log.d("FirebaseErrors", "$exception")
                                Toast.makeText(this, "Something went wrong $exception", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Re-authentication failed
                    val exception = reauthTask.exception
                    // Handle exception, check for specific error codes
                    Log.d("FirebaseErrors", "$exception")
                    Toast.makeText(this, "Re-authentication failed. Please log in again.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun showData() {
        val intent = intent

        fullNameUser = intent.getStringExtra("fullName") ?: ""
        emailUser = intent.getStringExtra("email") ?: ""
        usernameUser = intent.getStringExtra("username") ?: ""
        phoneNumberUser = intent.getStringExtra("phoneNumber") ?: ""

        editFullName.setText(fullNameUser)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPhoneNumber.setText(phoneNumberUser)
    }

    private fun isFullNameChanged(): Boolean {
        if (fullNameUser != editFullName.text.toString()) {
            firestore.collection("Users").document(auth.currentUser?.uid ?: "")
                .update("FullName", editFullName.text.toString())
            fullNameUser = editFullName.text.toString()
            return true
        }
        return false
    }

    private fun isUsernameChanged(): Boolean {
        if (usernameUser != editUsername.text.toString()) {
            firestore.collection("Users").document(auth.currentUser?.uid ?: "")
                .update("Username", editUsername.text.toString())
            usernameUser = editUsername.text.toString()
            return true
        }
        return false
    }

    private fun isPhoneNumberChanged(): Boolean {
        if (phoneNumberUser != editPhoneNumber.text.toString()) {
            firestore.collection("Users").document(auth.currentUser?.uid ?: "")
                .update("PhoneNumber", editPhoneNumber.text.toString())
            phoneNumberUser = editPhoneNumber.text.toString()
            return true
        }
        return false
    }

    private fun isEmailChanged(): Boolean {
        if (emailUser != editEmail.text.toString()) {
            firestore.collection("Users").document(auth.currentUser?.uid ?: "")
                .update("Email", editEmail.text.toString())
            emailUser = editEmail.text.toString()
            return true
        }
        return false
    }
}