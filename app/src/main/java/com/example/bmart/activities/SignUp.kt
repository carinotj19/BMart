package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import com.example.bmart.R
import com.example.bmart.models.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText
    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextPhoneNumber: TextInputEditText
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var buttonReg2: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var finalEmail: String
    private lateinit var finalPassword: String
    private lateinit var errorTextView: TextView
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()

        buttonReg = findViewById(R.id.signup_button1)
        buttonReg2  = findViewById(R.id.signup_button2)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextConfirmPassword = findViewById(R.id.confirm_password)
        editTextUsername = findViewById(R.id.username)
        editTextFirstName = findViewById(R.id.first_name)
        editTextLastName = findViewById(R.id.last_name)
        editTextPhoneNumber = findViewById(R.id.phone_number)
        errorTextView = findViewById(R.id.error_text)

        progressBar = findViewById(R.id.progress_bar)

        val textView: TextView = findViewById(R.id.log_in)
        textView.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        viewFlipper = findViewById(R.id.viewFlipper)

        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            editTextEmail.isEnabled = false
            editTextPassword.isEnabled = false
            editTextConfirmPassword.isEnabled = false

            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            val confirmPassword: String = editTextConfirmPassword.text.toString()

            if (validation1(email, password, confirmPassword)) {

                finalEmail = email
                finalPassword = password
                // Proceed to Step 2
                viewFlipper.showNext()
                progressBar.visibility = View.GONE
                errorTextView.text = ""
                editTextEmail.isEnabled = true
                editTextPassword.isEnabled = true
                editTextConfirmPassword.isEnabled = true
                buttonReg.isEnabled = true
            }
        }

        buttonReg2.setOnClickListener {
            val firstName: String = editTextFirstName.text.toString()
            val lastName: String = editTextLastName.text.toString()
            val phoneNumber: String = editTextPhoneNumber.text.toString()
            val username: String = editTextUsername.text.toString()
            // Validate Step 2 data
            if (validation2(phoneNumber, firstName, lastName)) {
                progressBar.visibility = View.VISIBLE
                editTextFirstName.isEnabled = false
                editTextLastName.isEnabled = false
                editTextPhoneNumber.isEnabled = false
                buttonReg2.isEnabled = false
                val fullName = "$firstName $lastName"
                auth.createUserWithEmailAndPassword(finalEmail, finalPassword)
                    .addOnCompleteListener(this) { task ->
                        val uid = auth.currentUser!!.uid
                        val df: DocumentReference = fireStore.collection("Users").document(uid)
                        val userInfo: MutableMap<String, Any> = HashMap()
                        userInfo["Email"] = finalEmail
                        userInfo["PhoneNumber"] = phoneNumber
                        userInfo["FullName"] = fullName
                        userInfo["Username"] = username
                        userInfo["Role"] = "user"
                        df.set(userInfo)
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                        val user = auth.currentUser
                            Toast.makeText(
                                baseContext,
                                "Account Creation Success.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext,
                                "Account Creation failed",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        progressBar.visibility = View.GONE
                        editTextFirstName.isEnabled = true
                        editTextLastName.isEnabled = true
                        editTextPhoneNumber.isEnabled = true
                        buttonReg2.isEnabled = true
                    }
            }
        }


    }

    private fun validation2(phoneNumber: String, firstName: String, lastName: String): Boolean {

        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            errorTextView.text = "All fields are required"
            return false
        }
        return true
    }

    private fun validation1(email: String, password: String, confirmPassword: String): Boolean {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            errorTextView.text = "All fields are required"
            return false
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password Must be 8 character long", Toast.LENGTH_SHORT).show()
            errorTextView.text = "Password Must be 8 character long"
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password not the same", Toast.LENGTH_SHORT).show()
            errorTextView.text = "Password not the same"
            return false
        }
        errorTextView.visibility = View.GONE
        return true
    }
}