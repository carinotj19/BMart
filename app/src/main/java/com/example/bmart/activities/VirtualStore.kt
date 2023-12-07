package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.bmart.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VirtualStore : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var myStore: CardView
    private var hasStore: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virtual_store)

        myStore = findViewById(R.id.my_store_card)
        val createStore: CardView = findViewById(R.id.create_store_card)

        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "home")
            startActivity(intent)
            finish()
        }

        checkVirtualStoreExistence()

        myStore.setOnClickListener{
            if (hasStore){
                val intent = Intent(this, MyStore::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please create a store first!", Toast.LENGTH_SHORT).show()
            }
        }

        createStore.setOnClickListener{
            val intent = Intent(this, CreateStore::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkVirtualStoreExistence() {
        val currentUserUid = auth.currentUser?.uid

        if (currentUserUid != null) {
            val virtualStoreRef = fireStore.collection("Users").document(currentUserUid)
                .collection("VirtualStore")

            virtualStoreRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val hasVirtualStore = !task.result?.isEmpty!!

                    if (hasVirtualStore) {
                        // User has the "VirtualStore" subcollection
                        myStore.isEnabled = true
                        hasStore = true
                        Toast.makeText(this, "Existing Store!", Toast.LENGTH_SHORT).show()
                    } else {
                        // User does not have the "VirtualStore" subcollection
                        myStore.isEnabled = false
                        hasStore = false
                        Toast.makeText(this, "No Existing Store!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the error if needed
                    Toast.makeText(this, "Error checking VirtualStore: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}