package com.example.bmart.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.adapters.ItemAdapter
import com.example.bmart.activities.ItemDetails
import com.example.bmart.models.ItemModel
import com.example.bmart.R
import com.google.firebase.firestore.FirebaseFirestore
import io.github.serpro69.kfaker.faker

class Items : Fragment(), ItemAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemModelArrayList: MutableList<ItemModel>
    private lateinit var itemAdapter: ItemAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var noVendorsMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        itemAdapter = ItemAdapter(emptyList(), this)
        recyclerView.adapter = itemAdapter

        progressBar = view.findViewById(R.id.progressBar)

        val spinner = view.findViewById<Spinner>(R.id.spinnerCategory)
        val categories = arrayOf("Category 1", "Category 2", "Category 3", "Category 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                Toast.makeText(requireContext(), "Selected Category: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing was selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onItemClick(item: ItemModel) {
        val intent = Intent(requireContext(), ItemDetails::class.java)
        intent.putExtra("ITEMS_NAME", item.itemsName)
        intent.putExtra("ITEMS_IMAGE", item.itemsImage)
        intent.putExtra("ITEMS_PRICE", item.itemsPrice)
        intent.putExtra("VENDOR_NAME", item.vendorName)
        intent.putExtra("DOCUMENT_ID", item.documentId)
        val docid = item.documentId
        Log.d("DocumentOD", "$docid")
        startActivity(intent)
    }
    private fun dataInitialize() {
        val db = FirebaseFirestore.getInstance()
        val vendorsCollection = db.collection("Vendors")
        // List to store all items across vendors
        val allItems = mutableListOf<ItemModel>()

        // Query all vendors
        vendorsCollection.get()
            .addOnSuccessListener { vendorQuerySnapshot ->
                for (vendorDocument in vendorQuerySnapshot) {
                    // Get the reference to the "Items" subcollection for each vendor
                    val itemsCollection = vendorDocument.reference.collection("Items")

                    // Query all items for the current vendor
                    itemsCollection.get()
                        .addOnSuccessListener { itemsQuerySnapshot ->
                            // Iterate through items and add them to the list
                            for (itemDocument in itemsQuerySnapshot) {
                                val item = itemDocument.toObject(ItemModel::class.java)
                                allItems.add(item)
                            }

                            if (allItems.isEmpty()) {
                                // No vendors available, show a message
                                noVendorsMessage.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                            }

                            // Continue with the next steps, e.g., filtering duplicates and displaying in RecyclerView
                            filterAndDisplayItems(allItems)
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                            Log.w(TAG, "Error querying items for vendor ${vendorDocument.id}", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.w(TAG, "Error querying vendors", exception)
            }
    }

    private fun displayUniqueItems(uniqueItemsList: List<ItemModel>) {
        itemAdapter.updateData(uniqueItemsList)
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    // In your dataInitialize function, call the displayUniqueItems function after filtering
    private fun filterAndDisplayItems(allItems: List<ItemModel>) {
        // Use a HashSet to filter out duplicates based on some criteria, e.g., item name
        val uniqueItemsSet = HashSet<String>()
        val uniqueItemsList = mutableListOf<ItemModel>()

        for (item in allItems) {
            if (uniqueItemsSet.add(item.itemsName)) {
                // Item name is not a duplicate, add to the filtered list
                uniqueItemsList.add(item)
            }
        }

        // Continue with the next steps, e.g., displaying unique items in RecyclerView
        displayUniqueItems(uniqueItemsList)
    }
}