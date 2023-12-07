package com.example.bmart.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.models.VendorModel
import com.example.bmart.R
import com.example.bmart.activities.VendorDetails
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.serpro69.kfaker.faker
import kotlin.random.Random


class Vendors : Fragment(), VendorAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorModelArrayList: MutableList<VendorModel>
    private lateinit var vendorAdapter : VendorAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noVendorsMessage: TextView

    private lateinit var favoriteStates: Map<String, Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendors, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteStates = SharedPreferencesHelper.loadFavoriteStates(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        vendorAdapter = VendorAdapter(emptyList(), this, favoriteStates)
        recyclerView.adapter = vendorAdapter

        noVendorsMessage = view.findViewById(R.id.noVendorsMessage)

        val spinner = view.findViewById<Spinner>(R.id.spinnerCategory)
        val categories = arrayOf("Category 1", "Category 2", "Category 3", "Category 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        progressBar = view.findViewById(R.id.progressBar)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                Toast.makeText(requireContext(), "Selected Category: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing was selected", Toast.LENGTH_SHORT).show()
            }
        }

        dataInitialize()
        fetchVendorsData()
        fetchUserFavorites()
    }
    private fun fetchUserFavorites() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = FirebaseFirestore.getInstance().collection("Users").document(userId)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val favorites = documentSnapshot["FavoriteVendors"] as? List<String>
                        updateFavoriteStatus(favorites)
                    } else {
                        // User document does not exist or is empty
                        Log.w(TAG, "User document does not exist or is empty")
                        Toast.makeText(requireActivity(), "User document does not exist or is empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure when fetching user document
                    Log.w(TAG, "Error getting user favorites", exception)
                    Toast.makeText(requireActivity(), "Error getting user favorites: ${exception}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // User is not authenticated
            Log.w(TAG, "User is not authenticated")
            Toast.makeText(requireActivity(), "User is not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateFavoriteStatus(userFavorites: List<String>?) {
        if (userFavorites != null) {
            for (vendor in vendorModelArrayList) {
                vendor.isFavorite = userFavorites.contains(vendor.documentId)
            }
            vendorAdapter.updateData(vendorModelArrayList)
        } else {
            // User favorites list is null
            Log.w(TAG, "User favorites list is null")
        }
    }
    private fun fetchVendorsData() {
        val db = FirebaseFirestore.getInstance()
        val vendorsCollection = db.collection("Vendors")
        vendorModelArrayList = mutableListOf<VendorModel>()

        vendorsCollection.get()
            .addOnSuccessListener { result ->

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
                Log.w(TAG, "Error getting vendors data", exception)
                Toast.makeText(requireContext(), "Error fetching vendors data", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
    }
    override fun onItemClick(position: Int) {
        val clickedItem = vendorModelArrayList[position]
        val intent = Intent(requireContext(), VendorDetails::class.java)
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

    private fun generateRandomDouble(): Double {
        val randomNumber = Random.nextDouble(1.0, 5.0)
        return String.format("%.1f", randomNumber).toDouble()
    }
    private fun dataInitialize() {
//        val db = FirebaseFirestore.getInstance()
//        val vendorsCollection = db.collection("Vendors")
//        vendorModelArrayList = mutableListOf()
//        val faker = faker { }
//
//        for (i in 0 until 20) {
//            val vendorName = faker.name.name()
//            val vendorRating = generateRandomDouble()
//            val vendorLocation = faker.address.country()
//            val defaultImageUrl =
//                "https://firebasestorage.googleapis.com/v0/b/bmart-179b3.appspot.com/o/defaultVendorImage.jpg?alt=media&token=bb7b47e0-8089-4f3f-a737-eb9580aa0ed4"
//            // Create a vendor model
//            val vendorModel = VendorModel(
//                vendorName,
//                defaultImageUrl,
//                vendorRating,
//                vendorLocation,
//            )
//
//            vendorsCollection.add(vendorModel)
//                .addOnSuccessListener { documentReference ->
//                    // Set the documentId field after adding the vendor to the collection
//                    val updatedVendorModel = vendorModel.copy(documentId = documentReference.id)
//                    vendorModelArrayList.add(updatedVendorModel)
//
//                    // Add items for the vendor using the documentId
//                    addItemsForVendor(documentReference.id, vendorName)
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding vendor document", e)
//                }
//        }
    }
    private fun addItemsForVendor(vendorDocumentId: String, vendorName: String) {
        val db = FirebaseFirestore.getInstance()
        val itemsCollection = db.collection("Vendors").document(vendorDocumentId).collection("Items")
        val faker = faker {}

        for (j in 0 until 10) {
            val itemName = faker.food.vegetables()
            val itemImage = "https://firebasestorage.googleapis.com/v0/b/bmart-179b3.appspot.com/o/ITEM.png?alt=media&token=578a6d74-b875-469e-8793-5cb4b27e7a8e" // Replace with your image URL

            // Create an item model
            val itemModel = ItemModel(
                vendorName,
                itemName,
                itemImage,
            )

            // Add the item to the "Items" collection
            itemsCollection.add(itemModel)
                .addOnSuccessListener { itemDocumentReference ->
                    Log.d(TAG, "Item document added with ID: ${itemDocumentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding item document", e)
                }
        }
    }
 }