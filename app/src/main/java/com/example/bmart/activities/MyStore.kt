package com.example.bmart.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.bmart.R
import com.example.bmart.activities.vsActivities.AddProducts
import com.example.bmart.activities.vsActivities.Orders
import com.example.bmart.activities.vsActivities.ViewStore
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MyStore : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var viewStall: CardView
    private lateinit var addProducts: CardView
    private lateinit var orders: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_store)

        viewStall = findViewById(R.id.view_stall_card)
        addProducts = findViewById(R.id.add_products_card)
        orders = findViewById(R.id.orders_card)

        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val currentUserUid = auth.currentUser?.uid
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, VirtualStore::class.java)
            startActivity(intent)
            finish()
        }

        viewStall.setOnClickListener {
            val intent = Intent(this, ViewStore::class.java)
            intent.putExtra("DOCUMENT_ID", currentUserUid)
            startActivity(intent)
            finish()
        }

        addProducts.setOnClickListener {
            showAddDialog()
        }

        orders.setOnClickListener {
            val intent = Intent(this, Orders::class.java)
            intent.putExtra("DOCUMENT_ID", currentUserUid)
            startActivity(intent)
            finish()
        }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_add_product, null)

        AlertDialog.Builder(this)
            .setTitle("Add Product")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}