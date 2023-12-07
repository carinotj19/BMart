package com.example.bmart.activities

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.ItemAdapter
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.models.ItemModel
import com.example.bmart.models.VendorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class VendorDetails : AppCompatActivity(), ItemAdapter.OnItemClickListener  {
    private var isHeartFilled: Boolean = false
    private lateinit var heartIcon: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter : ItemAdapter
    private lateinit var itemModelArrayList: MutableList<ItemModel>
    private lateinit var documentId: String
    private lateinit var userDocumentId: String
    private lateinit var vendorName: TextView
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)
        vendorName = findViewById(R.id.vendor_name)
        val vendorRating: TextView = findViewById(R.id.vendor_rating)
        val vendorProfile: ImageView = findViewById(R.id.vendor_profile)
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        heartIcon = findViewById(R.id.heart_icon)

        isHeartFilled = intent.getBooleanExtra("VENDOR_IS_FAVORITE", false)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "cart")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }
        heartIcon.setOnClickListener {
            isHeartFilled = !isHeartFilled
            updateHeartIcon()
        }

        vendorName.text = intent.getStringExtra("VENDORS_NAME")
        vendorRating.text = intent.getDoubleExtra("VENDORS_RATING", 0.0).toString()
        val profileImage = intent.getStringExtra("VENDOR_PROFILE")
        Picasso.get().load(profileImage).into(vendorProfile)

        userDocumentId = auth.currentUser?.uid ?: ""

        documentId = intent.getStringExtra("VENDOR_DOCUMENT_ID") ?: ""
        fetchItemsForVendor(documentId)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        itemAdapter = ItemAdapter(emptyList(), this)
        recyclerView.adapter = itemAdapter
    }

    private fun fetchItemsForVendor(vendorDocumentId: String) {
        val db = FirebaseFirestore.getInstance()
        val itemsCollection = db.collection("Vendors").document(vendorDocumentId).collection("Items")
        itemModelArrayList = mutableListOf<ItemModel>()

        itemsCollection.get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val item = document.toObject(ItemModel::class.java)
                    itemModelArrayList.add(item.copy(documentId = document.id))
                }

                // Update the adapter with the fetched items
                itemAdapter.updateData(itemModelArrayList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.w(TAG, "Error getting items for vendor.", exception)
            }
    }
    private fun addVendorToFavorites(userID: String, vendorID: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users").document(userID)

        // Update the user's favoriteVendors field with the new vendorID
        userRef.update("FavoriteVendors", FieldValue.arrayUnion(vendorID))
            .addOnSuccessListener {
                // Successfully added to favorites
                Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.e(ContentValues.TAG, "Error adding vendor to favorites: ${e.message}", e)
                Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeVendorFromFavorites(userID: String, vendorID: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users").document(userID)

        // Update the user's favoriteVendors field to remove the vendorID
        userRef.update("FavoriteVendors", FieldValue.arrayRemove(vendorID))
            .addOnSuccessListener {
                // Successfully removed from favorites
                Toast.makeText(this, "Vendor removed from favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.e(TAG, "Error removing vendor from favorites: ${e.message}", e)
                Toast.makeText(this, "Error removing vendor from favorites", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateHeartIcon() {
        if (isHeartFilled) {
            // Set the heart icon to filled state
            heartIcon.setImageResource(R.drawable.favorite_icon_filled)
            heartIcon.setColorFilter(ContextCompat.getColor(this, R.color.red))
            addVendorToFavorites(userDocumentId, documentId)
        } else {
            // Set the heart icon to outlined state
            heartIcon.setImageResource(R.drawable.favorite_icon_outlined)
            heartIcon.setColorFilter(ContextCompat.getColor(this, R.color.red))
            removeVendorFromFavorites(userDocumentId, documentId)
        }
    }

    override fun onItemClick(item: ItemModel) {
        val name = item.itemsName;
        Toast.makeText(this, "CLICKED $name", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ItemClick::class.java)
        intent.putExtra("ITEM_NAME", item.itemsName)
        intent.putExtra("ITEM_PRICE", item.itemsPrice)
        intent.putExtra("ITEM_PROFILE", item.itemsImage)
        intent.putExtra("VENDOR_NANE", "${vendorName.text}")
        setResult(RESULT_OK, intent)
        startActivity(intent)
    }
}