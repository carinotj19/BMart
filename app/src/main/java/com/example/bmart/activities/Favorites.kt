package com.example.bmart.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.VendorModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Favorites : AppCompatActivity(), VendorAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorModelArrayList: MutableList<VendorModel>
    private lateinit var vendorAdapter : VendorAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noVendorsMessage: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference


    private lateinit var favoriteStates: Map<String, Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        favoriteStates = SharedPreferencesHelper.loadFavoriteStates(this)
        val backBtn: ImageButton = findViewById(R.id.back_button)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "Home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        vendorAdapter = VendorAdapter(emptyList(), this, favoriteStates)
        recyclerView.adapter = vendorAdapter

        progressBar = findViewById(R.id.progressBar)
        noVendorsMessage = findViewById(R.id.noVendorsMessage)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val user = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        val favoritesCollection = user?.let { db.collection("Users").document(it) }

        favoritesCollection?.get()?.addOnSuccessListener { documentSnapshot ->
            val favoriteVendorIds = documentSnapshot.get("FavoriteVendors") as List<String>?

            if (favoriteVendorIds != null && favoriteVendorIds.isNotEmpty()) {
                // At least one favorite vendor ID is found, fetch corresponding vendor data
                fetchVendorsData(favoriteVendorIds)
            } else {
                // No favorite vendors available, show a message
                noVendorsMessage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }?.addOnFailureListener { e ->
            // Handle failure
            Log.e("FavoritesActivity", "Error getting favorite vendors", e)
        }
    }

    private fun fetchVendorsData(favoriteVendorIds: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val vendorsCollection = db.collection("Vendors")
        vendorModelArrayList = mutableListOf()
        Log.d("Fav", "It am in the function ${vendorModelArrayList}")
        vendorsCollection.whereIn(FieldPath.documentId(), favoriteVendorIds)
            .get()
            .addOnSuccessListener { result ->
                Log.d("Fav", "It am in the function ${result}, ${FieldPath.documentId()}, ${favoriteVendorIds}")
                for (document in result) {
                    val vendorModel = document.toObject(VendorModel::class.java)
                    vendorModelArrayList.add(vendorModel.copy(documentId = document.id))
                }
                if (vendorModelArrayList.isEmpty()) {
                    // No vendors available, show a message
                    noVendorsMessage.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                } else {
                    // Update the UI with the vendors
                    vendorAdapter.updateData(vendorModelArrayList)
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FavoritesActivity", "Error getting vendors data", exception)
                Toast.makeText(this, "Error fetching vendors data", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
    }

    override fun onItemClick(position: Int) {
        val clickedItem = vendorModelArrayList[position]
        val intent = Intent(this, VendorDetails::class.java)
        val vendorName = clickedItem.vendorsName
        val vendorRating = clickedItem.vendorsRating
        val vendorImage = clickedItem.vendorsImage
        val documentId = clickedItem.documentId
        val isFavorite = clickedItem.isFavorite
        Log.d("isFav", "$isFavorite")
        intent.putExtra("VENDORS_NAME", vendorName)
        intent.putExtra("VENDORS_RATING", vendorRating)
        intent.putExtra("VENDOR_PROFILE", vendorImage)
        intent.putExtra("VENDOR_DOCUMENT_ID", clickedItem.documentId)
        intent.putExtra("VENDOR_IS_FAVORITE", isFavorite)
        startActivity(intent)
    }
}