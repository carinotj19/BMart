package com.example.bmart.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.VendorModel
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ItemDetails : AppCompatActivity(), VendorAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorModelArrayList: MutableList<VendorModel>
    private lateinit var vendorAdapter: VendorAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var noVendorsMessage: TextView

    private lateinit var favoriteStates: Map<String, Boolean>

    // List to store vendor document IDs
    private val vendorDocumentIds: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        favoriteStates = SharedPreferencesHelper.loadFavoriteStates(this)
        val itemName: TextView = findViewById(R.id.item_name)
        val itemImage: ImageView = findViewById(R.id.item_image)
        val backBtn: ImageButton = findViewById(R.id.back_button)
        progressBar = findViewById(R.id.progressBar)
        noVendorsMessage = findViewById(R.id.noVendorsMessage)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "cart")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        val img = intent.getStringExtra("ITEMS_IMAGE")
        val itemNameFromIntent = intent.getStringExtra("ITEMS_NAME")
        itemName.text = itemNameFromIntent
        Picasso.get().load(img).into(itemImage)

        dataInitialize()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        vendorAdapter = VendorAdapter(emptyList(), this, favoriteStates)
        recyclerView.adapter = vendorAdapter

        fetchVendorsForItem(itemNameFromIntent)
    }

    private fun fetchVendorsForItem(itemName: String?) {
        if (itemName.isNullOrEmpty()) {
            return
        }

        val db = FirebaseFirestore.getInstance()
        val vendorsCollection = db.collection("Vendors")
        vendorModelArrayList = mutableListOf<VendorModel>()
        vendorDocumentIds.clear()

        vendorsCollection.get()
            .addOnSuccessListener { vendorQuerySnapshot ->
                for (vendorDocument in vendorQuerySnapshot) {
                    val itemsCollection = vendorDocument.reference.collection("Items")
                    itemsCollection.whereEqualTo("itemsName", itemName)
                        .get()
                        .addOnSuccessListener { itemsQuerySnapshot ->
                            if (!itemsQuerySnapshot.isEmpty) {
                                // Add the vendor document ID to the list
                                vendorDocumentIds.add(vendorDocument.id)

                                val vendorModel = vendorDocument.toObject(VendorModel::class.java)
                                vendorModelArrayList.add(vendorModel)
                            }
                            if (vendorModelArrayList.isEmpty()) {
                                noVendorsMessage.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                            } else {
                                vendorAdapter.updateData(vendorModelArrayList)
                                progressBar.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error querying items for vendor ${vendorDocument.id}", exception)
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error querying vendors", exception)
            }
    }
    private fun dataInitialize() {
//        vendorModelArrayList = arrayListOf()
//        val faker = faker {  }
//
//        for (i in 0 until 20) {
//            val vendorName = faker.name.name()
//            val vendorRating = Random.nextFloat() * (5.0 - 1.0) + 1.0
//            val df = DecimalFormat("#.#")
//            df.roundingMode = RoundingMode.CEILING
//            val vendorLocation = faker.address.country()
//
//            val vendorModel = VendorModel(
//                vendorName,
//                "https://firebasestorage.googleapis.com/v0/b/bmart-179b3.appspot.com/o/defaultVendorImage.jpg?alt=media&token=bb7b47e0-8089-4f3f-a737-eb9580aa0ed4",
//                df.format(vendorRating).toFloat(),
//                vendorLocation,
//            )
//            vendorModelArrayList.add(vendorModel)
//        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem = vendorModelArrayList[position]
        val intent = Intent(this, VendorDetails::class.java)
        val vendorName = clickedItem.vendorsName
        val vendorRating = clickedItem.vendorsRating
        val vendorImage = clickedItem.vendorsImage
        val documentId = vendorDocumentIds[position]
        Log.d("docID", "$documentId")
        intent.putExtra("VENDORS_NAME", vendorName)
        intent.putExtra("VENDORS_RATING", vendorRating)
        intent.putExtra("VENDOR_PROFILE", vendorImage)
        intent.putExtra("VENDOR_DOCUMENT_ID", documentId)
        startActivity(intent)
        finish()
    }
}